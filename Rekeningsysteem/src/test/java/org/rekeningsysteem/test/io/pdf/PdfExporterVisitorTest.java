package org.rekeningsysteem.test.io.pdf;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.io.pdf.PdfExporterVisitor;
import org.rekeningsysteem.properties.PropertiesWorker;

@RunWith(MockitoJUnitRunner.class)
public class PdfExporterVisitorTest {

	private PdfExporterVisitor visitor;
	@Mock private File mockedFile;
	@Mock private PropertiesWorker properties;

	@Before
	public void setUp() {
		this.visitor = new PdfExporterVisitor(this.properties);
		this.visitor.setSaveLocation(this.mockedFile);
	}

	@Test
	public void testSetGetSaveLocation() {
		File file = mock(File.class);
		this.visitor.setSaveLocation(file);
		assertEquals(file, this.visitor.getSaveLocation());
	}
}
