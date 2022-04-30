package org.rekeningsysteem.test.properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	
	private static final Path path = Paths.get("src", "test", "resources", "test.resources");

	private PropertiesWorker worker;
	@Mock private Properties properties;
	@Mock private PropertyKey key;
	@Mock private Logger logger;

	@BeforeClass
	public static void setUpClass() throws IOException {
		if (!Files.exists(path)) {
			try (OutputStream stream = new FileOutputStream(path.toFile())) {
				//Makes new File
			}
		}
	}

	@Before
	public void setUp() throws IOException {
		this.worker = PropertiesWorker.getInstance(this.properties, path, this.logger);
		verify(this.properties).load((InputStream) any());
	}

	@AfterClass
	public static void tearDownClass() throws IOException {
		Files.deleteIfExists(path);
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
		PropertiesWorker.getInstance(this.properties, path, this.logger);

		verify(this.logger).error(anyString(), (Throwable) any());
	}

	@Test
	public void testLoadFile_FileNotFound() {
		PropertiesWorker.getInstance(this.properties, Paths.get("bestaatniet.txt"), this.logger);

		verify(this.logger).error(anyString(), (Throwable) any());
	}

	@Test
	public void testSaveFails() throws IOException {
		doThrow(new IOException("")).when(this.properties).store((OutputStream) any(), anyString());
		this.worker.setProperty("key", "value");

		verify(this.logger).error(anyString(), (Throwable) any());
	}
}
