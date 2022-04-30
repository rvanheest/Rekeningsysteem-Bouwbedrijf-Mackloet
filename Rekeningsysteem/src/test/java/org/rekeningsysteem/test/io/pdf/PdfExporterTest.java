package org.rekeningsysteem.test.io.pdf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.pdf.PdfExporterVisitor;

import java.nio.file.Path;

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
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testOfferteExport() throws Exception {
		Offerte mockedRekening = mock(Offerte.class);
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testParticulierFactuurExport() throws Exception {
		ParticulierFactuur mockedRekening = mock(ParticulierFactuur.class);
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testReparatiesFactuurExport() throws Exception {
		ReparatiesFactuur mockedRekening = mock(ReparatiesFactuur.class);
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}
}
