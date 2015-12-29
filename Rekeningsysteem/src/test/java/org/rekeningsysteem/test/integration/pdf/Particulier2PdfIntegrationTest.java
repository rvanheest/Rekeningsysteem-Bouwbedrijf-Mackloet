package org.rekeningsysteem.test.integration.pdf;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.ParticulierFactuur2;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon2;
import org.rekeningsysteem.data.particulier.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.pdf.PdfExporter;

// TODO ParticulierPdfIntegrationTest
public class Particulier2PdfIntegrationTest {

	private PdfExporter exporter;

	protected ItemList<ParticulierArtikel2> addArtikels1() {
		ItemList<ParticulierArtikel2> list = new ItemList<>();

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

		list.add(new EsselinkParticulierArtikel(sub1, 8, 19));
		list.add(new EsselinkParticulierArtikel(sub2, 1, 19));
		list.add(new EsselinkParticulierArtikel(sub3, 1, 19));
		list.add(new EsselinkParticulierArtikel(sub4, 1, 19));
		list.add(new EsselinkParticulierArtikel(sub5, 1, 19));
		list.add(new EsselinkParticulierArtikel(sub6, 1, 19));
		list.add(new EsselinkParticulierArtikel(sub7, 1, 19));
		list.add(new ParticulierArtikel2Impl("Stucloper + trapfolie", new Geld(15.00), 19));
		list.add(new ParticulierArtikel2Impl("Kitwerk", new Geld(149.50), 19));

		return list;
	}

	protected ItemList<AbstractLoon2> addLoon1() {
		ItemList<AbstractLoon2> list = new ItemList<>();

		list.add(new ProductLoon2("Uurloon à 38.50", 25, new Geld(38.50), 19));
		list.add(new ProductLoon2("test123", 12, new Geld(12.50), 19));
		list.add(new InstantLoon2("foobar", new Geld(40.00), 19));

		return list;
	}

	protected ItemList<ParticulierArtikel2> addArtikels2() {
		ItemList<ParticulierArtikel2> list = new ItemList<>();

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

		list.add(new EsselinkParticulierArtikel(sub1, 8, 21));
		list.add(new EsselinkParticulierArtikel(sub2, 1, 21));
		list.add(new EsselinkParticulierArtikel(sub3, 1, 21));
		list.add(new EsselinkParticulierArtikel(sub4, 1, 21));
		list.add(new EsselinkParticulierArtikel(sub5, 1, 21));
		list.add(new EsselinkParticulierArtikel(sub6, 1, 21));
		list.add(new EsselinkParticulierArtikel(sub7, 1, 21));
		list.add(new ParticulierArtikel2Impl("Stucloper + trapfolie", new Geld(15.00), 21));
		list.add(new ParticulierArtikel2Impl("Kitwerk", new Geld(149.50), 21));

		return list;
	}

	protected ItemList<AbstractLoon2> addLoon2() {
		ItemList<AbstractLoon2> list = new ItemList<>();

		list.add(new ProductLoon2("Uurloon à 38.50", 25, new Geld(38.50), 6));
		list.add(new ProductLoon2("test123", 12, new Geld(12.50), 6));
		list.add(new InstantLoon2("foobar", new Geld(40.00), 6));

		return list;
	}

	@Before
	public void setUp() {
		this.exporter = new PdfExporter(false);
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

		ItemList<ParticulierArtikel2> itemList = this.addArtikels2();
		itemList.addAll(this.addLoon2());

		ParticulierFactuur2 factuur = new ParticulierFactuur2(header, Currency.getInstance("EUR"),
				itemList);
		this.exporter.export(factuur, new File("src\\test\\resources\\pdf\\"
				+ "ParticulierFactuurTest123TwoBtw.pdf"));
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

		ItemList<ParticulierArtikel2> itemList = this.addArtikels1();
		itemList.addAll(this.addLoon1());

		ParticulierFactuur2 factuur = new ParticulierFactuur2(header, Currency.getInstance("EUR"),
				itemList);
		this.exporter.export(factuur, new File("src\\test\\resources\\pdf\\"
				+ "ParticulierFactuurTest123OneBtw.pdf"));
	}
}
