package com.github.rvanheest.rekeningsysteem.test;

import com.github.rvanheest.rekeningsysteem.businesslogic.DependencyInjection;
import org.apache.commons.configuration.PropertiesConfiguration;

public interface DependencyInjectionFixture extends ConfigurationFixture, DatabaseFixture {

  default PropertiesConfiguration initDependencyInjection() throws Exception {
    this.initDatabaseConnection().closeConnectionPool();

    PropertiesConfiguration configuration = this.getConfiguration();
    configuration.setProperty("database.url", String.format("jdbc:sqlite:%s", databaseFile()));

    DependencyInjection.setInstance(new DependencyInjection(configuration));

    return configuration;
  }

  default void destroyDependencyInjection() throws Exception {
    DependencyInjection.destroyInstance();
  }
}
