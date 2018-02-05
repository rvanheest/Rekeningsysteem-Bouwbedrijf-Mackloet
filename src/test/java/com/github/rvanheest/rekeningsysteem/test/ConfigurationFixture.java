package com.github.rvanheest.rekeningsysteem.test;

import com.github.rvanheest.rekeningsysteem.businesslogic.DependencyInjection;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface ConfigurationFixture extends TestSupportFixture {

  default PropertiesConfiguration getConfiguration() throws Exception {
    // TODO make private method in Java 9
    BiFunction<Path, Path, Path> copyIfNotExists = (source, target) -> {
      if (!Files.exists(target)) {
        if (Files.isRegularFile(source))
          Files.copy(source, target);
        else
          FileUtils.copyDirectory(source.toFile(), target.toFile());
      }
      return target;
    };
    Function<Path, Path> createIfNotExist = dir -> {
      if (!Files.exists(dir))
        Files.createDirectories(dir);
      return dir;
    };

    Path defaultOfferText = copyIfNotExists.apply(
        Paths.get(getClass().getClassLoader().getResource("offerText/DefaultOfferText.txt").toURI()),
        this.getTestDir().resolve("DefaultOfferText.txt"));
    Path pdfTemplate = copyIfNotExists.apply(
        Paths.get(getClass().getClassLoader().getResource("pdftemplate").toURI()),
        this.getTestDir().resolve("pdftemplate"));
    Path pdfStagingDir = createIfNotExist.apply(this.getTestDir().resolve("pdfstaging"));

    return new PropertiesConfiguration() {{
      setProperty("offer.defaulttext", defaultOfferText.toString());
      setProperty("pdf.dateformat", "dd-MM-yyyy");
      setProperty("pdf.template.mutation", pdfTemplate.resolve("MutatiesFactuur.tex").toString());
      setProperty("pdf.template.offer", pdfTemplate.resolve("Offerte.tex").toString());
      setProperty("pdf.template.normal", pdfTemplate.resolve("ParticulierFactuur.tex").toString());
      setProperty("pdf.template.repair", pdfTemplate.resolve("ReparatiesFactuur.tex").toString());
      setProperty("pdf.staging", pdfStagingDir.toString());
      setProperty("database.driverclassname", "org.sqlite.JDBC");
      setProperty("database.url", "jdbc:sqlite:database.db");
    }};
  }
}
