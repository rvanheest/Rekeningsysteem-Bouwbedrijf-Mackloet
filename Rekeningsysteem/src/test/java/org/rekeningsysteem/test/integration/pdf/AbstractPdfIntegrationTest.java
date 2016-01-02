package org.rekeningsysteem.test.integration.pdf;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.pdf.PdfExporter;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractPdfIntegrationTest {

	private PdfExporter exporter;
	private AbstractRekening rekening;
	private File file;
	@Mock private Logger logger;

	protected abstract AbstractRekening makeRekening();

	protected abstract File makeFile();

	@Before
	public void setUp() {
		this.exporter = new PdfExporter(false, this.logger);
		this.rekening = this.makeRekening();
		this.file = this.makeFile();
	}

	@Test
	public void testPdf() {
		this.exporter.export(this.rekening, this.file);

		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportWithError() throws Exception {
		AbstractRekening rekening = mock(AbstractRekening.class);
		File file = mock(File.class);
		doThrow(Exception.class).when(rekening).accept(anyObject());

		this.exporter.export(rekening, file);

		verify(this.logger).error(anyString(), any(Exception.class));
	}
}
