package org.rekeningsysteem.test.io.pdf;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;

import org.apache.logging.log4j.core.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.pdf.PdfExporterVisitor;

@RunWith(MockitoJUnitRunner.class)
public class PdfExporterTest {

	private PdfExporter exporter;
	@Mock private PdfExporterVisitor visitor;
	@Mock private Logger logger;

	@Before
	public void setUp() {
		this.exporter = new PdfExporter(this.visitor, this.logger);
	}

	@Test
	public void testExport() throws Exception {
		AbstractRekening mockedRekening = mock(AbstractRekening.class);
		File mockedFile = mock(File.class);

		this.exporter.export(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(mockedRekening).accept(eq(this.visitor));
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportWithException() throws Exception {
		AbstractRekening mockedRekening = mock(AbstractRekening.class);
		File mockedFile = mock(File.class);

		doThrow(new Exception()).when(mockedRekening).accept(eq(this.visitor));

		this.exporter.export(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(mockedRekening).accept(eq(this.visitor));
		verify(this.logger).error(anyString(), (Throwable) anyObject());
	}
}
