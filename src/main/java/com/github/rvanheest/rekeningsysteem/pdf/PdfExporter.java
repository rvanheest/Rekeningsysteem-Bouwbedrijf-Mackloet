package com.github.rvanheest.rekeningsysteem.pdf;

import com.github.rvanheest.rekeningsysteem.exception.PdfException;
import com.github.rvanheest.rekeningsysteem.model.MyMonetaryFormatter;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import de.nixosoft.jlr.JLRGenerator;
import de.nixosoft.jlr.JLROpener;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FilenameUtils;

import javax.money.format.MonetaryAmountFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PdfExporter {

  private final PdfDocumentVisitor documentVisitor;
  private final PdfTemplateVisitor templateVisitor;
  private final Path stagingDirectory;

  public PdfExporter(PropertiesConfiguration configuration, Locale locale) {
    this(configuration, locale, MyMonetaryFormatter.of(locale));
  }

  private PdfExporter(PropertiesConfiguration configuration, Locale locale, MonetaryAmountFormat moneyFormatter) {
    this(new PdfDocumentVisitor(configuration.getString("pdf.dateformat"), locale, moneyFormatter,
            new PdfListItemVisitor(moneyFormatter)), new PdfTemplateVisitor(configuration),
        Paths.get(configuration.getString("pdf.staging")));
  }

  public PdfExporter(PdfDocumentVisitor documentVisitor, PdfTemplateVisitor templateVisitor, Path stagingDirectory) {
    this.documentVisitor = documentVisitor;
    this.templateVisitor = templateVisitor;
    this.stagingDirectory = stagingDirectory;
  }

  public void export(AbstractDocument document, Path saveLocation) throws PdfException {
    Path template = this.getTemplate(document);
    Path templateDir = template.getParent();

    // fill placeholders in template
    PdfConverter converter = this.convert(document, templateDir);

    // convert template to *.tex file
    String fileName = String.format("%s-%s",
        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).replace(':', '-'),
        FilenameUtils.removeExtension(saveLocation.getFileName().toString()));
    Path resultTex = this.stagingDirectory.toAbsolutePath().resolve(fileName + ".tex");
    this.parse(converter, template, resultTex);

    // generate pdf from *.tex file
    this.generate(resultTex, saveLocation.getParent(), templateDir);
    this.renameFile(saveLocation, fileName);
  }

  private Path getTemplate(AbstractDocument document) throws PdfException {
    try {
      Path template = document.accept(this.templateVisitor).toAbsolutePath();
      if (Files.exists(template) && Files.isRegularFile(template)) {
        return template;
      }
      throw new PdfException(String.format("Template %s does not exist or isn't a file", template));
    }
    catch (Exception e) {
      throw new PdfException(String.format("Template for %s could not be found", document.getClass().getSimpleName()), e);
    }
  }

  private PdfConverter convert(AbstractDocument document, Path templateDir) throws PdfException {
    try {
      PdfConverter converter = new PdfConverter(templateDir);
      document.accept(this.documentVisitor).accept(converter);
      return converter;
    }
    catch (Exception e) {
      throw new PdfException("Converting document failed", e);
    }
  }

  private void parse(PdfConverter converter, Path template, Path resultTex) throws PdfException {
    try {
      if (!converter.parse(template.toFile(), resultTex.toFile())) {
        throw new PdfException("Parsing LaTeX file failed: " + converter.getErrorMessage());
      }
    }
    catch (IOException e) {
      throw new PdfException("Parsing LaTeX file failed", e);
    }
  }

  private void generate(Path resultTex, Path saveDir, Path templateDir) throws PdfException {
    try {
      JLRGenerator generator = new JLRGenerator();
      //                  deleteTempTex  deleteAux deleteLog
      generator.deleteTempFiles(true, true, true);
      if (!generator.generate(resultTex.toFile(), saveDir.toFile(), templateDir.toFile())) {
        throw new PdfException(generator.getErrorMessage());
      }
    }
    catch (IOException e) {
      throw new PdfException("Generating pdf file failed", e);
    }
  }

  private void renameFile(Path saveLocation, String fileName) throws PdfException {
    Path source = saveLocation.getParent().resolve(fileName + ".pdf");
    try {
      Files.move(source, saveLocation);
    }
    catch (IOException e) {
      throw new PdfException("Could not rename " + source + " to " + saveLocation, e);
    }
  }

  public void openPdf(Path saveLocation) throws PdfException {
    try {
      JLROpener.open(saveLocation.toFile());
    }
    catch (IOException e) {
      throw new PdfException(String.format("Unable to open the pdf at %s", saveLocation.toAbsolutePath()), e);
    }
  }
}
