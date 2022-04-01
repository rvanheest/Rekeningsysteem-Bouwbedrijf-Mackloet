package org.rekeningsysteem.test.properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.core.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

@RunWith(MockitoJUnitRunner.class)
public class PropertiesWorkerTest {
	
	private static final File file = new File("src\\test\\resources\\test.resources");

	private PropertiesWorker worker;
	@Mock private Properties properties;
	@Mock private PropertyKey key;
	@Mock private Logger logger;

	@BeforeClass
	public static void setUpClass() throws IOException {
		if (!file.exists()) {
			try (OutputStream stream = new FileOutputStream(file)) {
				//Makes new File
			}
		}
	}

	@Before
	public void setUp() throws IOException {
		this.worker = PropertiesWorker.getInstance(this.properties, file, this.logger);
		verify(this.properties).load((InputStream) any());
	}

	@AfterClass
	public static void tearDownClass() {
		if (file.exists()) {
			file.delete();
		}
	}

	@Test
	public void testSetProperties() throws IOException {
		when(this.key.getKey()).thenReturn("key");

		this.worker.setProperty(this.key, "value");

		verify(this.key).getKey();
		verify(this.properties).setProperty(eq("key"), eq("value"));
		verify(this.properties).store((OutputStream) any(), anyString());
	}

	@Test
	public void testGetValue() {
		when(this.key.getKey()).thenReturn("key");
		when(this.properties.getProperty(anyString())).thenReturn("value");

		assertEquals(Optional.of("value"), this.worker.getProperty(this.key));
	}
	
	@Test
	public void testGetValueNotPresent() {
		when(this.key.getKey()).thenReturn("key");
		when(this.properties.getProperty(anyString())).thenReturn(null);

		assertEquals(Optional.empty(), this.worker.getProperty(this.key));
	}

	@Test
	public void testLoadFails() throws IOException {
		doThrow(new IOException("")).when(this.properties).load((InputStream) any());
		PropertiesWorker.getInstance(this.properties, file, this.logger);

		verify(this.logger).error(anyString(), (Throwable) any());
	}

	@Test
	public void testLoadFile_FileNotFound() {
		PropertiesWorker.getInstance(this.properties, new File("bestaatniet.txt"), this.logger);

		verify(this.logger).error(anyString(), (Throwable) any());
	}

	@Test
	public void testSaveFails() throws IOException {
		doThrow(new IOException("")).when(this.properties).store((OutputStream) any(), anyString());
		this.worker.setProperty("key", "value");

		verify(this.logger).error(anyString(), (Throwable) any());
	}
}
