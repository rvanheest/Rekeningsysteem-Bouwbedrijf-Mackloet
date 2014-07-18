package org.rekeningsysteem.properties.guice;

import java.io.File;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class GraphicsPropertiesModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(File.class).annotatedWith(PropertiesFile.class)
				.toInstance(new File("src\\main\\resources\\graphics.properties"));
	}

	@Provides
	@PropertiesObject
	public Properties provideProperties() {
		return new Properties();
	}
}
