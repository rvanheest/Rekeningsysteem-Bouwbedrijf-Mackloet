package com.github.rvanheest.rekeningsysteem.test;

import com.github.rvanheest.rekeningsysteem.businesslogic.DependencyInjection;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.util.function.Function;

public interface DependencyInjectionFixture extends ConfigurationFixture, DatabaseFixture {

  default PropertiesConfiguration initDependencyInjection() throws Exception {
    return this.initDependencyInjection(DependencyInjection::new);
  }

  default PropertiesConfiguration initDependencyInjection(Function<PropertiesConfiguration, DependencyInjection> construct)
      throws Exception {
    this.initDatabaseConnection().closeConnectionPool();

    PropertiesConfiguration configuration = this.getConfiguration();
    configuration.setProperty("database.url", String.format("jdbc:sqlite:%s", databaseFile()));

    DependencyInjection.setInstance(construct.apply(configuration));

    return configuration;
  }

  default void destroyDependencyInjection() throws Exception {
    DependencyInjection.destroyInstance();
  }
}
