package org.rekeningsysteem.io.pdf;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier2.ParticulierFactuur2;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;

import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;

public class PdfExporterVisitor implements RekeningVisitor {

	private final boolean autoOpen;
	private final PropertiesWorker properties;
	private final ListItemVisitor<List<List<String>>> itemVisitor;
	private File saveLocation;

	public PdfExporterVisitor(ListItemVisitor<List<List<String>>> itemVisitor) {
		this(true, PropertiesWorker.getInstance(), itemVisitor);
	}

	public PdfExporterVisitor(boolean autoOpen, ListItemVisitor<List<List<String>>> itemVisitor) {
		this(autoOpen, PropertiesWorker.getInstance(), itemVisitor);
	}

	public PdfExporterVisitor(boolean autoOpen, PropertiesWorker properties,
			ListItemVisitor<List<List<String>>> itemVisitor) {
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
	
	private void general(File templateTex, Consumer<PdfConverter> convert) throws PdfException, IOException {
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

	@Override
	public void visit(AangenomenFactuur factuur) throws Exception {
		Optional<File> templateTex = this.properties.getProperty(PropertyModelEnum.PDF_AANGENOMEN_TEMPLATE).map(File::new);
		if (templateTex.isPresent()) {
			this.general(templateTex.get(), this.convert(factuur));
		}
	}

	@Override
	public void visit(MutatiesFactuur factuur) throws Exception {
		Optional<File> templateTex = this.properties.getProperty(PropertyModelEnum.PDF_MUTATIES_TEMPLATE).map(File::new);
		if (templateTex.isPresent()) {
			this.general(templateTex.get(), this.convert(factuur));
		}
	}

	@Override
	public void visit(Offerte offerte) throws Exception {
		Optional<File> templateTex = this.properties.getProperty(PropertyModelEnum.PDF_OFFERTE_TEMPLATE).map(File::new);
		if (templateTex.isPresent()) {
			this.general(templateTex.get(), this.convert(offerte));
		}
	}

	@Override
	@Deprecated
	public void visit(ParticulierFactuur factuur) throws Exception {
		Optional<File> templateTex = this.properties.getProperty(PropertyModelEnum.PDF_PARTICULIER_TEMPLATE).map(File::new);
		if (templateTex.isPresent()) {
			this.general(templateTex.get(), this.convert(factuur));
		}
	}

	@Override
	public void visit(ParticulierFactuur2 factuur) throws Exception {
		Optional<File> templateTex = this.properties.getProperty(PropertyModelEnum.PDF_PARTICULIER_2_TEMPLATE).map(File::new);
		if (templateTex.isPresent()) {
			this.general(templateTex.get(), this.convert(factuur));
		}
	}

	@Override
	public void visit(ReparatiesFactuur factuur) throws Exception {
		Optional<File> templateTex = this.properties.getProperty(PropertyModelEnum.PDF_REPARATIES_TEMPLATE).map(File::new);
		if (templateTex.isPresent()) {
			this.general(templateTex.get(), this.convert(factuur));
		}
	}

	private Consumer<PdfConverter> convertFactuurHeader(FactuurHeader header) {
		return converter -> {
    		Debiteur debiteur = header.getDebiteur();
    		converter.replace("DebiteurNaam", debiteur.getNaam());
    		converter.replace("DebiteurStraat", debiteur.getStraat());
    		converter.replace("DebiteurNummer", debiteur.getNummer());
    		converter.replace("DebiteurPostcode", debiteur.getPostcode());
    		converter.replace("DebiteurPlaats", debiteur.getPlaats());
    		converter.replace("HasDebiteurBtwNummer", debiteur.getBtwNummer().isPresent());
    		converter.replace("DebiteurBtwNummer", debiteur.getBtwNummer().orElse(""));
    
    		converter.replace("Factuurnummer", header.getFactuurnummer().orElse(""));
    		this.properties.getProperty(PropertyModelEnum.DATE_FORMAT)
    				.map(format -> header.getDatum().format(DateTimeFormatter.ofPattern(format)))
    				.ifPresent(datum -> converter.replace("Datum", datum));
		};
	}

	private Consumer<PdfConverter> convertOmschrFactuurHeader(OmschrFactuurHeader header) {
		return this.convertFactuurHeader(header)
				.andThen(converter -> converter.replace("Omschrijving", header.getOmschrijving()));
	}

	private Consumer<PdfConverter> convertTotalen(Totalen totalen) {
		return converter -> {
			converter.replace("SubTotaalBedrag", totalen.getSubtotaal().formattedString());
			converter.replace("btwList", totalen.getBtw().entrySet()
					.stream()
					.sorted(Map.Entry.comparingByKey())
					.map(entry -> Arrays.asList(String.valueOf(entry.getKey()),
							entry.getValue().formattedString()))
					.collect(Collectors.toList()));
			converter.replace("TotaalBedrag", totalen.getTotaal().formattedString());
		};
	}

	private Consumer<PdfConverter> convert(AangenomenFactuur factuur) {
		return this.convertOmschrFactuurHeader(factuur.getFactuurHeader())
				.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
				.andThen(converter -> converter.replace("aangenomenList", factuur.getItemList()
						.stream()
						.flatMap(item -> item.accept(this.itemVisitor).stream())
        				.collect(Collectors.toList())))
        		.andThen(this.convertTotalen(factuur.getTotalen()));
	}

	private Consumer<PdfConverter> convert(MutatiesFactuur factuur) {
		return this.convertFactuurHeader(factuur.getFactuurHeader())
				.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
				.andThen(converter -> converter.replace("bonList", factuur.getItemList()
						.stream()
        				.flatMap(item -> item.accept(this.itemVisitor).stream())
        				.collect(Collectors.toList())))
        		.andThen(converter -> converter.replace("TotaalBedrag", factuur.getTotalen().getTotaal().formattedString()));
	}

	private Consumer<PdfConverter> convert(Offerte offerte) {
		return this.convertFactuurHeader(offerte.getFactuurHeader())
				.andThen(converter -> converter.replace("Tekst", offerte.getTekst()))
				.andThen(converter -> converter.replace("Ondertekenen", "" + offerte.isOndertekenen()));
	}

	private Consumer<PdfConverter> convert(ParticulierFactuur factuur) {
		return this.convertOmschrFactuurHeader(factuur.getFactuurHeader())
				.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
				.andThen(converter -> converter.replace("artikelList", factuur.getItemList()
						.stream()
						.flatMap(artikel -> artikel.accept(this.itemVisitor).stream())
						.collect(Collectors.toList())))
				.andThen(converter -> converter.replace("loonList", factuur.getLoonList()
						.stream()
						.flatMap(loon -> loon.accept(this.itemVisitor).stream())
						.collect(Collectors.toList())))
				.andThen(this.convertTotalen(factuur.getTotalen()));
	}

	private Consumer<PdfConverter> convert(ParticulierFactuur2 factuur) {
		return this.convertOmschrFactuurHeader(factuur.getFactuurHeader())
				.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
				.andThen(converter -> converter.replace("artikelList", factuur.getItemList()
						.stream()
						.flatMap(artikel -> artikel.accept(this.itemVisitor).stream())
						.collect(Collectors.toList())))
				.andThen(this.convertTotalen(factuur.getTotalen()));
	}

	private Consumer<PdfConverter> convert(ReparatiesFactuur factuur) {
		return this.convertFactuurHeader(factuur.getFactuurHeader())
				.andThen(converter -> converter.replace("Valuta", factuur.getCurrency().getSymbol()))
				.andThen(converter -> converter.replace("bonList", factuur.getItemList()
						.stream()
        				.flatMap(item -> item.accept(this.itemVisitor).stream())
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
