package org.rekeningsysteem.test.io.pdf;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.io.pdf.PdfExporterVisitor;
import org.rekeningsysteem.io.pdf.PdfListItemVisitor;
import org.rekeningsysteem.properties.PropertiesWorker;

import java.nio.file.Path;

@RunWith(MockitoJUnitRunner.class)
public class PdfExporterVisitorTest {

	private PdfExporterVisitor visitor;
	@Mock private Path mockedPath;
	@Mock private PropertiesWorker properties;
	@Mock private PdfListItemVisitor itemVisitor;

	@Before
	public void setUp() {
		this.visitor = new PdfExporterVisitor(false, this.properties, this.itemVisitor);
		this.visitor.setSaveLocation(this.mockedPath);
	}

	@Test
	public void testSetGetSaveLocation() {
		Path path = mock(Path.class);
		this.visitor.setSaveLocation(path);
		assertEquals(path, this.visitor.getSaveLocation());
	}
}
