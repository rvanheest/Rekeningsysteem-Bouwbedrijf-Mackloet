package org.rekeningsysteem.test.integration.pdf;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.pdf.PdfExporter;

@RunWith(MockitoJUnitRunner.class)
public class ParticulierPdfIntegrationTest {

	private PdfExporter exporter;
	@Mock private Logger logger;

	protected ItemList<ParticulierArtikel> addArtikels1() {
		ItemList<ParticulierArtikel> list = new ItemList<>();

		EsselinkArtikel sub1 = new EsselinkArtikel("2018021117", "Product 1", 1, "Zak",
				new Geld(5.16));
		EsselinkArtikel sub2 = new EsselinkArtikel("2003131360", "Product 2", 1, "zak",
				new Geld(129.53));
		EsselinkArtikel sub3 = new EsselinkArtikel("2003131060", "Product 3", 1, "set",
				new Geld(35.96));
		EsselinkArtikel sub4 = new EsselinkArtikel("2003131306", "Product 4", 1, "zak",
				new Geld(9.47));
		EsselinkArtikel sub5 = new EsselinkArtikel("4010272112", "Product 5", 1, "Stuks",
				new Geld(17.18));
		EsselinkArtikel sub6 = new EsselinkArtikel("2009200131", "Product 6", 1, "Stuks",
				new Geld(6.84));
		EsselinkArtikel sub7 = new EsselinkArtikel("2009200105", "Product 7", 1, "Stuks",
				new Geld(7.44));

		list.add(new GebruiktEsselinkArtikel(sub1, 8, 19));
		list.add(new GebruiktEsselinkArtikel(sub2, 1, 19));
		list.add(new GebruiktEsselinkArtikel(sub3, 1, 19));
		list.add(new GebruiktEsselinkArtikel(sub4, 1, 19));
		list.add(new GebruiktEsselinkArtikel(sub5, 1, 19));
		list.add(new GebruiktEsselinkArtikel(sub6, 1, 19));
		list.add(new GebruiktEsselinkArtikel(sub7, 1, 19));
		list.add(new AnderArtikel("Stucloper + trapfolie", new Geld(15.00), 19));
		list.add(new AnderArtikel("Kitwerk", new Geld(149.50), 19));

		return list;
	}

	protected ItemList<AbstractLoon> addLoon1() {
		ItemList<AbstractLoon> list = new ItemList<>();

		list.add(new ProductLoon("Uurloon à 38.50", 25, new Geld(38.50), 19));
		list.add(new ProductLoon("test123", 12, new Geld(12.50), 19));
		list.add(new InstantLoon("foobar", new Geld(40.00), 19));

		return list;
	}

	protected ItemList<ParticulierArtikel> addArtikels2() {
		ItemList<ParticulierArtikel> list = new ItemList<>();

		EsselinkArtikel sub1 = new EsselinkArtikel("2018021117", "Product 1", 1, "Zak",
				new Geld(5.16));
		EsselinkArtikel sub2 = new EsselinkArtikel("2003131360", "Product 2", 1, "zak",
				new Geld(129.53));
		EsselinkArtikel sub3 = new EsselinkArtikel("2003131060", "Product 3", 1, "set",
				new Geld(35.96));
		EsselinkArtikel sub4 = new EsselinkArtikel("2003131306", "Product 4", 1, "zak",
				new Geld(9.47));
		EsselinkArtikel sub5 = new EsselinkArtikel("4010272112", "Product 5", 1, "Stuks",
				new Geld(17.18));
		EsselinkArtikel sub6 = new EsselinkArtikel("2009200131", "Product 6", 1, "Stuks",
				new Geld(6.84));
		EsselinkArtikel sub7 = new EsselinkArtikel("2009200105", "Product 7", 1, "Stuks",
				new Geld(7.44));

		list.add(new GebruiktEsselinkArtikel(sub1, 8, 21));
		list.add(new GebruiktEsselinkArtikel(sub2, 1, 21));
		list.add(new GebruiktEsselinkArtikel(sub3, 1, 21));
		list.add(new GebruiktEsselinkArtikel(sub4, 1, 21));
		list.add(new GebruiktEsselinkArtikel(sub5, 1, 21));
		list.add(new GebruiktEsselinkArtikel(sub6, 1, 21));
		list.add(new GebruiktEsselinkArtikel(sub7, 1, 21));
		list.add(new AnderArtikel("Stucloper + trapfolie", new Geld(15.00), 21));
		list.add(new AnderArtikel("Kitwerk", new Geld(149.50), 21));

		return list;
	}

	protected ItemList<AbstractLoon> addLoon2() {
		ItemList<AbstractLoon> list = new ItemList<>();

		list.add(new ProductLoon("Uurloon à 38.50", 25, new Geld(38.50), 6));
		list.add(new ProductLoon("test123", 12, new Geld(12.50), 6));
		list.add(new InstantLoon("foobar", new Geld(40.00), 6));

		return list;
	}

	@Before
	public void setUp() {
		this.exporter = new PdfExporter(false, this.logger);
	}

	@Test
	public void testExportTwoBtw() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
				"Place");
		LocalDate datum = LocalDate.of(2011, 4, 2);
		String factuurnummer = "22011";
		String omschrijving = "Voor u verrichte werkzaamheden betreffende renovatie "
				+ "badkamervloer i.v.m. lekkage";
		OmschrFactuurHeader header = new OmschrFactuurHeader(debiteur, datum, factuurnummer,
				omschrijving);

		ItemList<ParticulierArtikel> itemList = this.addArtikels2();
		itemList.addAll(this.addLoon2());

		ParticulierFactuur factuur = new ParticulierFactuur(header, Currency.getInstance("EUR"),
				itemList);
		this.exporter.export(factuur, new File("src\\test\\resources\\pdf\\"
				+ "ParticulierFactuurTest123TwoBtw.pdf"));

		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportOneBtw() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
				"Place");
		LocalDate datum = LocalDate.of(2011, 4, 2);
		String factuurnummer = "22011";
		String omschrijving = "Voor u verrichte werkzaamheden betreffende renovatie "
				+ "badkamervloer i.v.m. lekkage";
		OmschrFactuurHeader header = new OmschrFactuurHeader(debiteur, datum, factuurnummer,
				omschrijving);

		ItemList<ParticulierArtikel> itemList = this.addArtikels1();
		itemList.addAll(this.addLoon1());

		ParticulierFactuur factuur = new ParticulierFactuur(header, Currency.getInstance("EUR"),
				itemList);
		this.exporter.export(factuur, new File("src\\test\\resources\\pdf\\"
				+ "ParticulierFactuurTest123OneBtw.pdf"));

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
