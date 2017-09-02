package com.github.rvanheest.rekeningsysteem.pdf;

import com.github.rvanheest.rekeningsysteem.exception.PdfException;
import com.github.rvanheest.rekeningsysteem.model.MyMonetaryFormatter;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;
import org.apache.commons.configuration.PropertiesConfiguration;

import javax.money.format.MonetaryAmountFormat;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class PdfExporter {

  private final PdfDocumentVisitor documentVisitor;
  private final PdfTemplateVisitor templateVisitor;
  private final Path stagingDirectory;

  public PdfExporter(PropertiesConfiguration configuration, Locale locale) {
    this(configuration, locale, MyMonetaryFormatter.of(locale));
  }

  private PdfExporter(PropertiesConfiguration configuration, Locale locale, MonetaryAmountFormat moneyFormatter) {
    this(
        new PdfDocumentVisitor(configuration.getString("pdf.dateformat"), locale, moneyFormatter, new PdfListItemVisitor(moneyFormatter)),
        new PdfTemplateVisitor(configuration),
        Paths.get(configuration.getString("pdf.staging")));
  }

  public PdfExporter(PdfDocumentVisitor documentVisitor, PdfTemplateVisitor templateVisitor, Path stagingDirectory) {
    this.documentVisitor = documentVisitor;
    this.templateVisitor = templateVisitor;
    this.stagingDirectory = stagingDirectory;
  }

  public void export(AbstractDocument document, Path saveLocation) throws Exception {
    Path template = document.accept(this.templateVisitor);
    Path templateDir = template.getParent();

    // fill placeholders in template
    PdfConverter converter = new PdfConverter(templateDir);
    document.accept(this.documentVisitor).accept(converter);

    // convert template to *.tex file
    Path resultTex = this.stagingDirectory.toAbsolutePath().resolve(saveLocation.getFileName());
    if (!converter.parse(template.toFile(), resultTex.toFile())) {
      throw new PdfException(converter.getErrorMessage());
    }

    // generate pdf from *.tex file
    JLRGenerator generator = new JLRGenerator();
    //                  deleteTempTex  deleteAux deleteLog
    generator.deleteTempFiles(true, true, true);
    if (!generator.generate(resultTex.toFile(), saveLocation.getParent().toFile(), templateDir.toFile())) {
      throw new PdfException(generator.getErrorMessage());
    }
  }

  public void openPdf(Path saveLocation) throws IOException {
    JLROpener.open(saveLocation.toFile());
  }
}
