package com.github.rvanheest.rekeningsysteem.test;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface ConfigurationFixture extends TestSupportFixture {

  default PropertiesConfiguration getConfiguration() throws URISyntaxException, IOException, ConfigurationException {
    Path properties = Files.copy(
        Paths.get(getClass().getClassLoader().getResource("debug-config/application.properties").toURI()),
        this.getTestDir().resolve("application.properties"));

    return new PropertiesConfiguration(properties.toFile());
  }
}
