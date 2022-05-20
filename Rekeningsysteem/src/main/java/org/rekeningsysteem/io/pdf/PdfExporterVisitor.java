package org.rekeningsysteem.io.pdf;

import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;
import org.apache.commons.io.FileUtils;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.Document;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

import javax.money.CurrencyUnit;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PdfExporterVisitor {

	private final boolean autoOpen;
	private final PropertiesWorker properties;
	private final PdfListItemVisitor itemVisitor;
	private Path saveLocation;

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

	public Path getSaveLocation() {
		return this.saveLocation;
	}

	public void setSaveLocation(Path saveLocation) {
		this.saveLocation = saveLocation;
	}

	private void general(Path templateTex, Consumer<PdfConverter> convert) throws PdfException {
		try {
			String pdfName = this.saveLocation.getFileName().toString();
			pdfName = pdfName.substring(0, pdfName.lastIndexOf("."));

			Path tempTemplateDir = Paths.get("resources", "LaTeX", "temp").toAbsolutePath();
			if (!Files.exists(tempTemplateDir)) {
				Files.createDirectory(tempTemplateDir);
			}
			Path resultTex = tempTemplateDir.resolve(pdfName + ".tex");

			Path templateDir = templateTex.getParent();

			PdfConverter converter = new PdfConverter(templateDir);
			convert.accept(converter);
			this.parse(converter, templateTex, resultTex);
			this.generate(new JLRGenerator(), resultTex, templateDir);

			FileUtils.deleteDirectory(tempTemplateDir.toFile());

			if (this.autoOpen) {
				JLROpener.open(this.saveLocation.toFile());
			}
		}
		catch (IOException e) {
			throw new PdfException(e.getMessage(), e);
		}
	}

	public void visit(Document rekening) throws PdfException {
		switch (rekening) {
			case MutatiesFactuur factuur -> this.visit(PropertyModelEnum.PDF_MUTATIES_TEMPLATE, this.convert(factuur));
			case Offerte offerte -> this.visit(PropertyModelEnum.PDF_OFFERTE_TEMPLATE, this.convert(offerte));
			case ParticulierFactuur factuur -> this.visit(PropertyModelEnum.PDF_PARTICULIER_TEMPLATE, this.convert(factuur));
			case ReparatiesFactuur factuur -> this.visit(PropertyModelEnum.PDF_REPARATIES_TEMPLATE, this.convert(factuur));
			default -> throw new IllegalStateException("Unexpected value: " + rekening);
		}
	}

	private void visit(PropertyModelEnum key, Consumer<PdfConverter> converter) throws PdfException {
		Optional<Path> templateTex = this.properties.getPathProperty(key);
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
			this.properties.getDateTimeFormatterProperty(PropertyModelEnum.DATE_FORMAT)
				.map(header.datum()::format)
				.ifPresent(datum -> converter.replace("Datum", datum));
		};
	}

	private Consumer<PdfConverter> visitCurrency(CurrencyUnit currency) {
		return converter -> converter.replace("Valuta",
				Currency.getInstance(currency.getCurrencyCode()).getSymbol());
	}

	private Consumer<PdfConverter> convertTotalen(Totalen totalen) {
		return converter -> {
			converter.replace("SubTotaalBedrag",
				totalen.nettoBtwPerPercentage().keySet().stream().anyMatch(btw -> btw.percentage() != 0.0)
					? Collections.singletonList(totalen.getSubtotaal().formattedString())
					: Collections.emptyList()
			);
			converter.replace("btwList", totalen.nettoBtwPerPercentage().entrySet()
				.stream()
				.sorted(Map.Entry.comparingByKey())
				.map(entry -> Arrays.asList(
					entry.getKey().formattedString(),
					entry.getValue().netto().formattedString(),
					entry.getValue().btw().formattedString() + (entry.getKey().verlegd() ? " (verlegd)" : "")
				))
				.collect(Collectors.toList()));
			converter.replace("TotaalBedrag", totalen.getTotaal().formattedString());
		};
	}

	private Consumer<PdfConverter> convert(MutatiesFactuur factuur) {
		return this.convertFactuurHeader(factuur.header())
			.andThen(this.visitCurrency(factuur.itemList().getCurrency()))
			.andThen(converter -> converter.replace("orderList", factuur.itemList()
				.stream()
				.map(this.itemVisitor::visit)
				.collect(Collectors.toList())
			))
			.andThen(converter -> converter.replace("TotaalBedrag", factuur.getTotalen().getTotaal().formattedString()));
	}

	private Consumer<PdfConverter> convert(Offerte offerte) {
		return this.convertFactuurHeader(offerte.header())
			.andThen(converter -> converter.replace("Tekst", offerte.tekst()))
			.andThen(converter -> converter.replace("Ondertekenen", String.valueOf(offerte.ondertekenen())));
	}

	private Consumer<PdfConverter> convert(ParticulierFactuur factuur) {
		return this.convertFactuurHeader(factuur.header())
			.andThen(converter -> converter.replace("Omschrijving", factuur.omschrijving()))
			.andThen(this.visitCurrency(factuur.itemList().getCurrency()))
			.andThen(converter -> converter.replace("artikelList", factuur.itemList()
				.stream()
				.map(particulierArtikel -> switch (particulierArtikel) {
					case AnderArtikel a -> this.itemVisitor.visit(a);
					case GebruiktEsselinkArtikel a -> this.itemVisitor.visit(a);
					case InstantLoon l -> this.itemVisitor.visit(l);
					case ProductLoon l -> this.itemVisitor.visit(l);
					default -> throw new IllegalStateException("Unexpected value: " + particulierArtikel);
				})
				.collect(Collectors.toList())
			))
			.andThen(this.convertTotalen(factuur.getTotalen()));
	}

	private Consumer<PdfConverter> convert(ReparatiesFactuur factuur) {
		return this.convertFactuurHeader(factuur.header())
			.andThen(this.visitCurrency(factuur.itemList().getCurrency()))
			.andThen(converter -> converter.replace("orderList", factuur.itemList()
				.stream()
				.map(this.itemVisitor::visit)
				.collect(Collectors.toList())
			))
			.andThen(converter -> converter.replace("TotaalBedrag", factuur.getTotalen().getTotaal().formattedString()));
	}

	private void parse(PdfConverter converter, Path templateTex, Path resultTex) throws PdfException, IOException {
		if (!converter.parse(templateTex.toFile(), resultTex.toFile())) {
			throw new PdfException(converter.getErrorMessage());
		}
	}

	private void generate(JLRGenerator generator, Path resultTex, Path templateDir) throws PdfException, IOException {
		generator.deleteTempFiles(true, true, true);

		if (!generator.generate(resultTex.toFile(), this.saveLocation.getParent().toFile(), templateDir.toFile())) {
			throw new PdfException(generator.getErrorMessage());
		}
	}
}
