package org.rekeningsysteem.test.io.pdf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.materiaal.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.pdf.PdfExporterVisitor;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Currency;

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
		MutatiesFactuur mockedRekening = new MutatiesFactuur(
				new FactuurHeader(
						new Debiteur("naam", "straat", "huisnummer", "postcode", "woonplaats"),
						LocalDate.now()
				),
				new ItemList<>(
						Currency.getInstance("EUR"),
						Arrays.asList(
								new MutatiesInkoopOrder("order1", "12345", new Geld(12)),
								new MutatiesInkoopOrder("order2", "56789", new Geld(34))
						)
				)
		);
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testOfferteExport() throws Exception {
		Offerte mockedRekening = new Offerte(
				new FactuurHeader(
						new Debiteur("naam", "straat", "huisnummer", "postcode", "woonplaats"),
						LocalDate.now()
				),
				"tekst voor de offerte",
				true
		);
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testParticulierFactuurExport() throws Exception {
		ParticulierFactuur mockedRekening = new ParticulierFactuur(
				new FactuurHeader(
						new Debiteur("naam", "straat", "huisnummer", "postcode", "woonplaats"),
						LocalDate.now()
				),
				"omschrijving bij de factuur",
				new ItemList<>(
						Currency.getInstance("EUR"),
						Arrays.asList(
								new AnderArtikel("artikel1", new Geld(12), new BtwPercentage(21, false)),
								new GebruiktEsselinkArtikel(new EsselinkArtikel("artikel2", "esselink omschrijving", 1, "eenheid", new Geld(34)), 3, new BtwPercentage(21, false)),
								new ProductLoon("loon1", 8, new Geld(45), new BtwPercentage(9, false)),
								new InstantLoon("loon2", new Geld(56), new BtwPercentage(9, false))
						)
				)
		);
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}

	@Test
	public void testReparatiesFactuurExport() throws Exception {
		ReparatiesFactuur mockedRekening = new ReparatiesFactuur(
				new FactuurHeader(
						new Debiteur("naam", "straat", "huisnummer", "postcode", "woonplaats"),
						LocalDate.now()
				),
				new ItemList<>(
						Currency.getInstance("EUR"),
						Arrays.asList(
								new ReparatiesInkoopOrder("order1", "12345", new Geld(12), new Geld(98)),
								new ReparatiesInkoopOrder("order2", "56789", new Geld(34), new Geld(76))
						)
				)
		);
		Path mockedPath = mock(Path.class);

		this.exporter.export(mockedRekening, mockedPath);

		verify(this.visitor).setSaveLocation(eq(mockedPath));
		verify(this.visitor).visit(mockedRekening);
	}
}
