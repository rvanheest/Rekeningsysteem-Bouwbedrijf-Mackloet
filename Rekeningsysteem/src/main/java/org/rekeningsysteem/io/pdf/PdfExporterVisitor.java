package org.rekeningsysteem.io.pdf;

import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;
import org.apache.commons.io.FileUtils;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PdfExporterVisitor {

	private final boolean autoOpen;
	private final PropertiesWorker properties;
	private final PdfListItemVisitor itemVisitor;
	private File saveLocation;

	public PdfExporterVisitor(PdfListItemVisitor itemVisitor) {
		this(true, PropertiesWorker.getInstance(), itemVisitor);
	}

	public PdfExporterVisitor(boolean autoOpen, PdfListItemVisitor itemVisitor) {
		this(autoOpen, PropertiesWorker.getInstance(), itemVisitor);
	}

	public PdfExporterVisitor(boolean autoOpen, PropertiesWorker properties, PdfListItemVisitor itemVisitor) {
		this.autoOpen = autoOpen;
		this.properties = properties;
		this.itemVisitor = itemVisitor;
	}

	public File getSaveLocation() {
		return this.saveLocation;
	}

	public void setSaveLocation(File saveLocation) {
		this.saveLocation = saveLocation;
	}

	private void general(File templateTex, Consumer<PdfConverter> convert) throws PdfException {
		try {
			String pdfName = this.saveLocation.getName();
			pdfName = pdfName.substring(0, pdfName.lastIndexOf("."));

			File tempTemplateDir = new File("resources\\LaTeX\\temp");
			if (!tempTemplateDir.exists()) {
				tempTemplateDir.mkdir();
			}
			File resultTex = new File(tempTemplateDir.getAbsolutePath() + "\\" + pdfName + ".tex");

			File templateDir = templateTex.getParentFile();

			PdfConverter converter = new PdfConverter(templateDir);
			convert.accept(converter);
			this.parse(converter, templateTex, resultTex);
			this.generate(new JLRGenerator(), resultTex, templateDir);

			FileUtils.deleteDirectory(tempTemplateDir);

			if (this.autoOpen) {
				JLROpener.open(this.saveLocation);
			}
		}
		catch (IOException e) {
			throw new PdfException(e.getMessage(), e);
		}
	}

	private Optional<File> getTemplate(PropertyModelEnum key) {
		return this.properties.getProperty(key).map(File::new);
	}

	public void visit(AbstractRekening rekening) throws PdfException {
		switch (rekening) {
			case MutatiesFactuur factuur -> this.visit(PropertyModelEnum.PDF_MUTATIES_TEMPLATE, this.convert(factuur));
			case Offerte offerte -> this.visit(PropertyModelEnum.PDF_OFFERTE_TEMPLATE, this.convert(offerte));
			case ParticulierFactuur factuur -> this.visit(PropertyModelEnum.PDF_PARTICULIER_TEMPLATE, this.convert(factuur));
			case ReparatiesFactuur factuur -> this.visit(PropertyModelEnum.PDF_REPARATIES_TEMPLATE, this.convert(factuur));
			default -> throw new IllegalStateException("Unexpected value: " + rekening);
		}
	}

	private void visit(PropertyModelEnum key, Consumer<PdfConverter> converter) throws PdfException {
		Optional<File> templateTex = this.getTemplate(key);
		if (templateTex.isPresent()) this.general(templateTex.get(), converter);
	}

	private Consumer<PdfConverter> convertFactuurHeader(FactuurHeader header) {
		return converter -> {
			Debiteur debiteur = header.debiteur();
			converter.replace("DebiteurNaam", debiteur.naam());
			converter.replace("DebiteurStraat", debiteur.straat());
			converter.replace("DebiteurNummer", debiteur.nummer());
			converter.replace("DebiteurPostcode", debiteur.postcode());
			converter.replace("DebiteurPlaats", debiteur.plaats());
			converter.replace("HasDebiteurBtwNummer", debiteur.btwNummer().isPresent());
			converter.replace("DebiteurBtwNummer", debiteur.btwNummer().orElse(""));

			converter.replace("Factuurnummer", header.factuurnummer().orElse(""));
			this.properties.getProperty(PropertyModelEnum.DATE_FORMAT)
				.map(format -> header.datum().format(DateTimeFormatter.ofPattern(format)))
				.ifPresent(datum -> converter.replace("Datum", datum));
		};
	}

	private Consumer<PdfConverter> convertOmschrFactuurHeader(OmschrFactuurHeader header) {
		return this.convertFactuurHeader(header)
			.andThen(converter -> converter.replace("Omschrijving", header.getOmschrijving()));
	}

	private Consumer<PdfConverter> convertTotalen(Totalen totalen) {
		return converter -> {
			converter.replace("SubTotaalBedrag",
				totalen.getBtwPercentages().stream().anyMatch(btw -> btw.percentage() != 0.0)
					? Collections.singletonList(totalen.getSubtotaal().formattedString())
					: Collections.emptyList()
			);
			converter.replace("btwList", totalen.getNettoBtwTuple().entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.map(entry -> Arrays.asList(
					entry.getKey().formattedString(),
					entry.getValue().netto().formattedString(),
					entry.getValue().btw().formattedString() + (entry.getKey().verlegd() ? " (verlegd)" : "")))
				.collect(Collectors.toList()));
			converter.replace("TotaalBedrag", totalen.getTotaal().formattedString());
		};
	}

	private Consumer<PdfConverter> convert(MutatiesFactuur factuur) {
		return this.convertFactuurHeader(factuur.getFactuurHeader())
			.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
			.andThen(converter -> converter.replace("orderList", factuur.getItemList()
				.stream()
				.map(this.itemVisitor::visit)
				.collect(Collectors.toList())))
			.andThen(converter -> converter.replace("TotaalBedrag", factuur.getTotalen().getTotaal().formattedString()));
	}

	private Consumer<PdfConverter> convert(Offerte offerte) {
		return this.convertFactuurHeader(offerte.getFactuurHeader())
			.andThen(converter -> converter.replace("Tekst", offerte.getTekst()))
			.andThen(converter -> converter.replace("Ondertekenen",
				String.valueOf(offerte.isOndertekenen())));
	}

	private Consumer<PdfConverter> convert(ParticulierFactuur factuur) {
		return this.convertOmschrFactuurHeader(factuur.getFactuurHeader())
			.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
			.andThen(converter -> converter.replace("artikelList", factuur.getItemList()
				.stream()
				.map(particulierArtikel -> switch (particulierArtikel) {
					case AnderArtikel a -> this.itemVisitor.visit(a);
					case GebruiktEsselinkArtikel a -> this.itemVisitor.visit(a);
					case InstantLoon l -> this.itemVisitor.visit(l);
					case ProductLoon l -> this.itemVisitor.visit(l);
					default -> throw new IllegalStateException("Unexpected value: " + particulierArtikel);
				})
				.collect(Collectors.toList())))
			.andThen(this.convertTotalen(factuur.getTotalen()));
	}

	private Consumer<PdfConverter> convert(ReparatiesFactuur factuur) {
		return this.convertFactuurHeader(factuur.getFactuurHeader())
			.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
			.andThen(converter -> converter.replace("orderList", factuur.getItemList()
				.stream()
				.map(this.itemVisitor::visit)
				.collect(Collectors.toList())))
			.andThen(converter -> converter.replace("TotaalBedrag", factuur.getTotalen().getTotaal().formattedString()));
	}

	private void parse(PdfConverter converter, File templateTex, File resultTex) throws PdfException, IOException {
		if (!converter.parse(templateTex, resultTex)) {
			throw new PdfException(converter.getErrorMessage());
		}
	}

	private void generate(JLRGenerator generator, File resultTex, File templateDir) throws PdfException, IOException {
		generator.deleteTempFiles(true, true, true);

		if (!generator.generate(resultTex, this.saveLocation.getParentFile(), templateDir)) {
			throw new PdfException(generator.getErrorMessage());
		}
	}
}
