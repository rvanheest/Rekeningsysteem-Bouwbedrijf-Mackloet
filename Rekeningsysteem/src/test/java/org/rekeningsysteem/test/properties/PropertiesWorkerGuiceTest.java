package org.rekeningsysteem.test.properties;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Optional;
import java.util.Properties;

import org.junit.Test;
import org.rekeningsysteem.logging.ConsoleLoggerModule;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.guice.PropertiesFile;
import org.rekeningsysteem.properties.guice.PropertiesObject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;

public class PropertiesWorkerGuiceTest {

	@Test
	public void testMakePropertiesWorker() {
		Injector injector = Guice.createInjector(new TestPropertiesModule(),
				new ConsoleLoggerModule());
		PropertiesWorker prop = injector.getInstance(PropertiesWorker.class);
		assertEquals(Optional.of("foobar"), prop.getProperty("test123"));
	}

	private class TestPropertiesModule extends AbstractModule {

		@Override
		protected void configure() {
			this.bind(File.class).annotatedWith(PropertiesFile.class)
					.toInstance(new File("src\\test\\resources\\testConfig.properties"));
		}
		
		@Provides
		@PropertiesObject
		public Properties provideProperties() {
			return new Properties();
		}
	}
}
