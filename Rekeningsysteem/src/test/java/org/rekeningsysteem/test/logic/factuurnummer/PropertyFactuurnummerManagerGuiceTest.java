package org.rekeningsysteem.test.logic.factuurnummer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.guice.FactuurnummerManagerFactory;
import org.rekeningsysteem.logic.factuurnummer.guice.PropertyFactuurnummerManagerModule;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

@RunWith(MockitoJUnitRunner.class)
public class PropertyFactuurnummerManagerGuiceTest {

	@Mock private PropertiesWorker propertiesWorker;
	@Mock private PropertyKey propertyKey;
	@Mock private Logger logger;

	@Test
	public void testMakePropertyFactuurnummerManager() {
		Injector injector = Guice.createInjector(new PropertyFactuurnummerManagerModule(),
				new TestModule());
		when(this.propertiesWorker.getProperty(eq(this.propertyKey))).thenReturn("12012");
		FactuurnummerManager manager = injector.getInstance(FactuurnummerManagerFactory.class)
				.create(this.propertyKey);
		
		assertEquals("1" + LocalDate.now().getYear(), manager.getFactuurnummer());
	}

	private class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			this.bind(PropertiesWorker.class)
					.toInstance(PropertyFactuurnummerManagerGuiceTest.this.propertiesWorker);
			this.bind(Logger.class)
					.toInstance(PropertyFactuurnummerManagerGuiceTest.this.logger);
		}
	}
}
