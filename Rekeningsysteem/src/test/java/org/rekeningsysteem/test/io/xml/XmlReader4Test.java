package org.rekeningsysteem.test.io.xml;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.XmlReader4;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

@RunWith(MockitoJUnitRunner.class)
public class XmlReader4Test {

	private XmlReader4 reader;

	@Before
	public void setUp() throws ParserConfigurationException {
		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.reader = new XmlReader4(documentBuilder);
	}

	@Test
	public void testLoadMutatiesFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml4\\mutatiesFactuurXMLTest.xml");

		FactuurHeader factuurHeader = new FactuurHeader(
			new Debiteur("Name", "Street", "Number", "ZipCode", "Place", "btw"),
			LocalDate.of(2011, 5, 9),
			"272011"
		);

		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("Ordernummer", "111390", new Geld(4971.96)));
		itemList.add(new MutatiesInkoopOrder("Ordernummer", "111477", new Geld(4820.96)));
		itemList.add(new MutatiesInkoopOrder("Ordernummer", "112308", new Geld(5510.74)));

		MutatiesFactuur expected = new MutatiesFactuur(factuurHeader, Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadOfferte() {
		File file = new File("src\\test\\resources\\xml\\xml4\\offerteXMLTest.xml");

		FactuurHeader factuurHeader = new FactuurHeader(
			new Debiteur("Dhr. M. Stolk", "Ring", "", "", "Nieuwe-Tonge"),
			LocalDate.of(2011, 8, 11),
			"107"
		);
		Offerte expected = new Offerte(factuurHeader, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce quis quam tortor.", false);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadParticulierFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml4\\particulierFactuurXMLTest.xml");

		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(
			new Debiteur("Name", "Street", "Number", "Zipcode", "Place"),
			LocalDate.of(2011, 4, 2),
			"22011",
			"Voor u verrichte werkzaamheden betreffende renovatie badkamervloer i.v.m. lekkage"
		);

		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("2018021117", "Product 1", 1, "Zak", new Geld(5.16)), 8.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("2003131360", "Product 2", 1, "zak", new Geld(129.53)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("2003131060", "Product 3", 1, "set", new Geld(35.96)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("2003131306", "Product 4", 1, "zak", new Geld(9.47)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("4010272112", "Product 5", 1, "Stuks", new Geld(17.18)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("2009200131", "Product 6", 1, "Stuks", new Geld(6.84)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("2009200105", "Product 7", 1, "Stuks", new Geld(7.44)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new AnderArtikel("Stucloper + trapfolie", new Geld(15.0), new BtwPercentage(21.0, false)));
		itemList.add(new AnderArtikel("Kitwerk", new Geld(149.5), new BtwPercentage(21.0, false)));
		itemList.add(new ProductLoon("Uurloon à 38.50", 25.0, new Geld(38.5), new BtwPercentage(6.0, false)));
		itemList.add(new ProductLoon("test123", 12.0, new Geld(12.5), new BtwPercentage(6.0, false)));
		itemList.add(new InstantLoon("foobar", new Geld(40.0), new BtwPercentage(6.0, false)));

		ParticulierFactuur expected = new ParticulierFactuur(factuurHeader, Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}

	@Test
	public void testLoadReparatiesFactuur() {
		File file = new File("src\\test\\resources\\xml\\xml4\\reparatiesFactuurXMLTest.xml");

		FactuurHeader factuurHeader = new FactuurHeader(
			new Debiteur("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber"),
			LocalDate.of(2011, 4, 5),
			"232011"
		);

		ItemList<ReparatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "110543", new Geld(77), new Geld(6.5)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111558", new Geld(77), new Geld(9)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111518", new Geld(57.75), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111660", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111563", new Geld(115.5), new Geld(13.5)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111625", new Geld(57.75), new Geld(15)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111764", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111751", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111745", new Geld(38.5), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111571", new Geld(57.75), new Geld(3.5)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111876", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111716", new Geld(154), new Geld(7.5)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111854", new Geld(154), new Geld(183.5)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111912", new Geld(38.5), new Geld(9)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111940", new Geld(154), new Geld(9)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111928", new Geld(77), new Geld(4.5)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111723", new Geld(115.5), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111963", new Geld(299.26), new Geld(448.88)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111739", new Geld(408.16), new Geld(136.52)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111091", new Geld(1451.27), new Geld(967.51)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111409", new Geld(2546.57), new Geld(1697.72)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111272", new Geld(3630.66), new Geld(2420.44)));
		itemList.add(new ReparatiesInkoopOrder("Ordernummer", "111148", new Geld(3878.2), new Geld(2585.46)));

		ReparatiesFactuur expected = new ReparatiesFactuur(factuurHeader,
			Currency.getInstance("EUR"), itemList);

		this.reader.load(file)
			.test()
			.assertValue(expected)
			.assertNoErrors()
			.assertComplete();
	}
}
