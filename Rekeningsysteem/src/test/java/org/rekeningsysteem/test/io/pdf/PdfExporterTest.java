package org.rekeningsysteem.test.io.pdf;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.exception.PdfException;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.pdf.PdfExporterVisitor;

import java.io.File;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PdfExporterTest {

	private PdfExporter exporter;
	@Mock private PdfExporterVisitor visitor;

	@Before
	public void setUp() {
		this.exporter = new PdfExporter(this.visitor);
	}

	@Test
	public void testMutatiesFactuurExport() throws Exception {
		MutatiesFactuur mockedRekening = mock(MutatiesFactuur.class);
		File mockedFile = mock(File.class);

		this.exporter.export(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testOfferteExport() throws Exception {
		Offerte mockedRekening = mock(Offerte.class);
		File mockedFile = mock(File.class);

		this.exporter.export(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testParticulierFactuurExport() throws Exception {
		ParticulierFactuur mockedRekening = mock(ParticulierFactuur.class);
		File mockedFile = mock(File.class);

		this.exporter.export(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testReparatiesFactuurExport() throws Exception {
		ReparatiesFactuur mockedRekening = mock(ReparatiesFactuur.class);
		File mockedFile = mock(File.class);

		this.exporter.export(mockedRekening, mockedFile);

		verify(this.visitor).setSaveLocation(eq(mockedFile));
		verify(this.visitor).visit(mockedRekening);
	}
}
