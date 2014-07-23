package org.rekeningsysteem.test.logic.factuurnummer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.logic.factuurnummer.FileFactuurnummerManager;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

@RunWith(MockitoJUnitRunner.class)
public class FileFactuurnummerManagerTest {

	private FileFactuurnummerManager manager;
	private static final String LOCATION = "src\\test\\resources\\testFactuurnummer.txt";
	private static int yearNow = LocalDate.now().getYear();
	@Mock private PropertiesWorker propertiesWorker;
	@Mock private PropertyKey key;
	@Mock private Logger logger;

	private void setFileContent(String content) {
		try {
			FileUtils.writeStringToFile(new File(LOCATION), content);
		}
		catch (IOException exception) {
			fail(exception.getMessage());
		}
	}

	@BeforeClass
	public static void beforeClass() {
		File file = new File(LOCATION);
		assertTrue(file.exists());
	}

	@Before
	public void setUp() {
		when(this.propertiesWorker.getProperty(eq(this.key))).thenReturn(Optional.of(LOCATION));
		this.manager = new FileFactuurnummerManager(this.propertiesWorker, this.key, this.logger);
	}

	@Test
	public void testGetFactuurnummerSameYear() throws IOException {
		this.setFileContent("12" + yearNow);
		assertEquals("12" + yearNow, this.manager.getFactuurnummer());
		assertEquals("13" + yearNow, FileUtils.readFileToString(new File(LOCATION)));

		assertEquals("12" + yearNow, this.manager.getFactuurnummer());
		assertEquals("13" + yearNow, FileUtils.readFileToString(new File(LOCATION)));
	}

	@Test
	public void testGetFactuurnummerOtherYear() throws IOException {
		this.setFileContent("25" + (yearNow - 2));
		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		assertEquals("2" + yearNow, FileUtils.readFileToString(new File(LOCATION)));

		assertEquals("1" + yearNow, this.manager.getFactuurnummer());
		assertEquals("2" + yearNow, FileUtils.readFileToString(new File(LOCATION)));
	}

	@Test
	public void testGetFactuurnummerNotExistingFile() {
		String fileName = "src\\test\\resources\\test.txt";
		when(this.propertiesWorker.getProperty((PropertyKey) anyObject()))
				.thenReturn(Optional.of(fileName));
		FileFactuurnummerManager ffm = new FileFactuurnummerManager(this.propertiesWorker,
				this.key, this.logger);

		assertEquals("1" + yearNow, ffm.getFactuurnummer());
		
		// clear file
		File file = new File(fileName);
		assertTrue(file.exists());
		file.delete();
	}
}
