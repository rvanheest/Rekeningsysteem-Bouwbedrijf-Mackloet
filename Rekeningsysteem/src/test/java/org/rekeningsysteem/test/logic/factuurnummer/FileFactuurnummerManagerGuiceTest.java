package org.rekeningsysteem.test.logic.factuurnummer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.logic.factuurnummer.guice.FactuurnummerManagerFactory;
import org.rekeningsysteem.logic.factuurnummer.guice.FileFactuurnummerManagerModule;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

@RunWith(MockitoJUnitRunner.class)
public class FileFactuurnummerManagerGuiceTest {

	private static final String file = "src\\test\\resources\\testFactuurnummer.txt";
	@Mock private PropertiesWorker propertiesWorker;
	@Mock private PropertyKey propertyKey;
	@Mock private Logger logger;
	
	@BeforeClass
	public static void setUpClass() throws IOException {
		FileUtils.writeStringToFile(new File(file), "12013");
	}

	@Test
	public void testMakeFileFactuurnummerManager() {
		Injector injector = Guice.createInjector(new FileFactuurnummerManagerModule(),
				new TestModule());
		when(this.propertiesWorker.getProperty(eq(this.propertyKey))).thenReturn(file);
		FactuurnummerManager manager = injector.getInstance(FactuurnummerManagerFactory.class)
				.create(this.propertyKey);
		
		assertEquals("1" + LocalDate.now().getYear(), manager.getFactuurnummer());
	}
	
	private class TestModule extends AbstractModule {

		@Override
		protected void configure() {
			this.bind(PropertiesWorker.class)
					.toInstance(FileFactuurnummerManagerGuiceTest.this.propertiesWorker);
			this.bind(Logger.class).toInstance(FileFactuurnummerManagerGuiceTest.this.logger);
		}
	}
}
