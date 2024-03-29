package org.rekeningsysteem.test.io.xml;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.XmlReader1;

public class XmlReader1Test {

	private XmlReader1 reader;

	@Before
	public void setUp() throws ParserConfigurationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.reader = new XmlReader1(builder);
	}

	@Test
	public void testLoadParticulierFactuur1() {
		File file = new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur1.xml");

		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2012, 8, 13), "302012", "TestOmschrijving");

		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("12345",
				"testomschr", 1, "Zak", new Geld(46.14)), 6.0, new BtwPercentage(19.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("1234",
				"testomschr2", 1, "zak", new Geld(5.95)), 6.0, new BtwPercentage(19.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("7985464",
				"testomschr3", 1, "emmer", new Geld(42.32)), 20.0, new BtwPercentage(19.0, false)));
		itemList.add(new AnderArtikel("Ander artikel", new Geld(50.00), new BtwPercentage(19.0, false)));
		itemList.add(new ProductLoon("Uurloon à 5,60", 20.0, new Geld(5.60), new BtwPercentage(19.0, false)));

		ParticulierFactuur expected = new ParticulierFactuur(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadParticulierFactuur2() {
		File file = new File("src\\test\\resources\\xml\\xml1\\ParticulierFactuur2.xml");

		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2013, 2, 23), "32013", "test123");

		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("456123",
				"testomschr", 1, "zak", new Geld(9.83)), 20.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("789456123",
				"testomschr2", 1, "emmer", new Geld(42.32)), 6.0, new BtwPercentage(21.0, false)));
		itemList.add(new AnderArtikel("test", new Geld(20.00), new BtwPercentage(21.0, false)));
		itemList.add(new ProductLoon("Uurloon à 6,50", 3.0, new Geld(6.50), new BtwPercentage(6.0, false)));

		ParticulierFactuur expected = new ParticulierFactuur(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadParticulierFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml1\\PartFactuur.xml");

		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2012, 1, 2), "12012", "Nieuwjaarstest 2011-2012");

		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("123456",
				"testomschr", 1, "stuks", new Geld(1078.80)), 12.0, new BtwPercentage(0.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("456789",
				"testomschr2", 1, "stuks", new Geld(1078.80)), 11.0, new BtwPercentage(0.0, false)));
		itemList.add(new ProductLoon("Uurloon à 2,50", 1.0, new Geld(2.50), new BtwPercentage(0.0, false)));

		ParticulierFactuur expected = new ParticulierFactuur(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadMutatiesFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml1\\MutatiesFactuur.xml");

		FactuurHeader factuurHeader = new FactuurHeader(new Debiteur("testnaam",
				"teststraat", "testnummer", "testpostcode", "testplaats", "testbtwnr"),
				LocalDate.of(2012, 2, 7), "72012");

		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("Ordernummer", "13151", new Geld(2135131.00)));

		MutatiesFactuur expected = new MutatiesFactuur(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadReparatiesFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml1\\ReparatiesFactuur.xml");

		FactuurHeader factuurHeader = new FactuurHeader(new Debiteur("testnaam",
				"teststraat", "testnummer", "testpostcode", "testplaats", "testbtwnr"),
				LocalDate.of(2012, 1, 3), "22012");

		ItemList<ReparatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "35343134", new Geld(50), new Geld(60)));

		ReparatiesFactuur expected = new ReparatiesFactuur(factuurHeader,
				Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadOfferte() {
		File file = new File("src\\test\\resources\\xml\\xml1\\Offerte.xml");

		FactuurHeader factuurHeader = new FactuurHeader(new Debiteur(
				"testnaam", "teststraat", "testnummer", "testpostcode", "testplaats"),
				LocalDate.of(2012, 8, 24), "62012");
		Offerte expected = new Offerte(factuurHeader, "dsafsdkljfaskljfpoj", true);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}
}
