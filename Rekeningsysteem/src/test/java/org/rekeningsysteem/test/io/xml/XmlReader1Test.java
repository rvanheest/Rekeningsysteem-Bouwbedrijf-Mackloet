package org.rekeningsysteem.test.io.xml;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.ParticulierFactuur2;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.XmlReader1;

public class XmlReader1Test {

	private XmlReader1 reader;
	private DocumentBuilder builder;

	@Before
	public void setUp() throws ParserConfigurationException {
		this.builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.reader = new XmlReader1(this.builder);
	}

	@Test
	public void testLoadParticulierFactuur1() {
		File file = new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur1.xml");

		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2012, 8, 13), "302012", "TestOmschrijving");

		ItemList<ParticulierArtikel2> itemList = new ItemList<>();
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("12345",
				"testomschr", 1, "Zak", new Geld(46.14)), 6.0, 19.0));
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("1234",
				"testomschr2", 1, "zak", new Geld(5.95)), 6.0, 19.0));
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("7985464",
				"testomschr3", 1, "emmer", new Geld(42.32)), 20.0, 19.0));
		itemList.add(new ParticulierArtikel2Impl("Ander artikel", new Geld(50.00), 19.0));
		itemList.add(new ProductLoon2("Uurloon à 5,60", 20.0, new Geld(5.60), 19.0));

		ParticulierFactuur2 expected = new ParticulierFactuur2(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file).forEach(rek -> assertEquals(expected, rek));
	}

	@Test
	public void testLoadParticulierFactuur2() {
		File file = new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur2.xml");

		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2013, 2, 23), "32013", "test123");

		ItemList<ParticulierArtikel2> itemList = new ItemList<>();
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("456123",
				"testomschr", 1, "zak", new Geld(9.83)), 20.0, 21.0));
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("789456123",
				"testomschr2", 1, "emmer", new Geld(42.32)), 6.0, 21.0));
		itemList.add(new ParticulierArtikel2Impl("test", new Geld(20.00), 21.0));
		itemList.add(new ProductLoon2("Uurloon à 6,50", 3.0, new Geld(6.50), 6.0));

		ParticulierFactuur2 expected = new ParticulierFactuur2(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file).forEach(rek -> assertEquals(expected, rek));
	}

	@Test
	public void testLoadParticulierFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml1\\PartFactuur.xml");

		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2012, 1, 2), "12012", "Nieuwjaarstest 2011-2012");

		ItemList<ParticulierArtikel2> itemList = new ItemList<>();
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("123456",
				"testomschr", 1, "stuks", new Geld(1078.80)), 12.0, 0.0));
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("456789",
				"testomschr2", 1, "stuks", new Geld(1078.80)), 11.0, 0.0));
		itemList.add(new ProductLoon2("Uurloon à 2,50", 1.0, new Geld(2.50), 0.0));

		ParticulierFactuur2 expected = new ParticulierFactuur2(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file).forEach(rek -> assertEquals(expected, rek));
	}

	@Test
	public void testLoadMutatiesFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml1\\MutatiesFactuur.xml");

		FactuurHeader factuurHeader = new FactuurHeader(new Debiteur("testnaam",
				"teststraat", "testnummer", "testpostcode", "testplaats", "testbtwnr"),
				LocalDate.of(2012, 2, 7), "72012");

		ItemList<MutatiesBon> itemList = new ItemList<>();
		itemList.add(new MutatiesBon("Bonnummer", "13151", new Geld(2135131.00)));

		MutatiesFactuur expected = new MutatiesFactuur(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file).forEach(rek -> assertEquals(expected, rek));
	}

	@Test
	public void testLoadReparatiesFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml1\\ReparatiesFactuur.xml");

		FactuurHeader factuurHeader = new FactuurHeader(new Debiteur("testnaam",
				"teststraat", "testnummer", "testpostcode", "testplaats", "testbtwnr"),
				LocalDate.of(2012, 1, 3), "22012");

		ItemList<ReparatiesBon> itemList = new ItemList<>();
		itemList.add(new ReparatiesBon("Bonnummer", "35343134", new Geld(50), new Geld(60)));

		ReparatiesFactuur expected = new ReparatiesFactuur(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file).forEach(rek -> assertEquals(expected, rek));
	}

	@Test
	public void testLoadOfferte() {
		File file = new File("src\\test\\resources\\xml\\xml1\\Offerte.xml");

		FactuurHeader factuurHeader = new FactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2012, 8, 24), "62012");
		Offerte expected = new Offerte(factuurHeader, "dsafsdkljfaskljfpoj", true);

		this.reader.load(file).forEach(rek -> assertEquals(expected, rek));
	}
}
