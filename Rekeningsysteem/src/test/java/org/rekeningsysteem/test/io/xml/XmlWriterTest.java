package org.rekeningsysteem.test.io.xml;

import org.junit.Before;
import org.junit.Test;
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
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.exception.XmlWriteException;
import org.rekeningsysteem.io.xml.XmlWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlWriterTest {

	private XmlWriter writer;

	@Before
	public void setUp() throws ParserConfigurationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		this.writer = new XmlWriter(builder, transformerFactory);
	}

	private void write(AbstractRekening rekening, String filename) throws XmlWriteException {
		File dir = new File("src\\test\\resources\\xml\\writer");
		if (!dir.exists()) assertTrue(dir.mkdir());
		
		File file = new File(dir, filename);
		assertTrue(file.delete());
		assertFalse(file.exists());

		this.writer.save(rekening, file);
		assertTrue(file.exists());
	}

	@Test
	public void testWriteMutatiesFactuurToXml() throws XmlWriteException {
		Debiteur debiteur = new Debiteur("name", "street", "number", "zipcode", "city", "vatNumber");
		LocalDate date = LocalDate.of(2017, 7, 30);
		FactuurHeader factuurHeader = new FactuurHeader(debiteur, date, "272011");

		Currency currency = Currency.getInstance("EUR");

		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("Bonnummer", "111390", new Geld(4971.96)));
		itemList.add(new MutatiesInkoopOrder("Bonnummer", "111477", new Geld(4820.96)));
		itemList.add(new MutatiesInkoopOrder("Bonnummer", "112308", new Geld(5510.74)));

		MutatiesFactuur factuur = new MutatiesFactuur(factuurHeader, currency, itemList);

		this.write(factuur, "MutatiesFactuur.xml");
	}

	@Test
	public void testWriteOfferteToXml() throws XmlWriteException {
		Debiteur debiteur = new Debiteur("name", "street", "number", "zipcode", "city", "vatNumber");
		LocalDate date = LocalDate.of(2017, 7, 30);
		FactuurHeader factuurHeader = new FactuurHeader(debiteur, date, "272011");
		Offerte offerte = new Offerte(factuurHeader, "text", true);

		this.write(offerte, "Offerte.xml");
	}

	@Test
	public void testWriteNormalInvoice() throws XmlWriteException {
		Debiteur debiteur = new Debiteur("name", "street", "number", "zipcode", "city", "vatNumber");
		LocalDate date = LocalDate.of(2017, 7, 30);
		String omschrijving = "Voor u verrichte werkzaamheden betreffende renovatie badkamervloer i.v.m. lekkage";
		OmschrFactuurHeader factuurHeader = new OmschrFactuurHeader(debiteur, date, "272011", omschrijving);

		Currency currency = Currency.getInstance("EUR");

		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel("Product 1", new EsselinkArtikel("2018021117", "Product 1", 1, "Zak", new Geld(5.16)), 8.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel("Product 2", new EsselinkArtikel("2003131360", "Product 2", 1, "zak", new Geld(129.53)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel("Product 3", new EsselinkArtikel("2003131060", "Product 3", 1, "set", new Geld(35.96)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel("Product 4", new EsselinkArtikel("2003131306", "Product 4", 1, "zak", new Geld(9.47)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel("Product 5", new EsselinkArtikel("4010272112", "Product 5", 1, "Stuks", new Geld(17.18)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel("Product 6", new EsselinkArtikel("2009200131", "Product 6", 1, "Stuks", new Geld(6.84)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new GebruiktEsselinkArtikel("Product 7", new EsselinkArtikel("2009200105", "Product 7", 1, "Stuks", new Geld(7.44)), 1.0, new BtwPercentage(21.0, false)));
		itemList.add(new AnderArtikel("Stucloper + trapfolie", new Geld(15.0), new BtwPercentage(21.0, false)));
		itemList.add(new AnderArtikel("Kitwerk", new Geld(149.5), new BtwPercentage(21.0, false)));
		itemList.add(new ProductLoon("Uurloon à 38.50", 25.0, new Geld(38.5), new BtwPercentage(6.0, false)));
		itemList.add(new ProductLoon("test123", 12.0, new Geld(12.5), new BtwPercentage(6.0, false)));
		itemList.add(new InstantLoon("foobar", new Geld(40.0), new BtwPercentage(6.0, false)));

		ParticulierFactuur factuur = new ParticulierFactuur(factuurHeader, currency, itemList);
		this.write(factuur, "ParticulierFactuur.xml");
	}

	@Test
	public void testWriteRepairsInvoice() throws XmlWriteException {
		Debiteur debiteur = new Debiteur("name", "street", "number", "zipcode", "city", "vatNumber");
		LocalDate date = LocalDate.of(2017, 7, 30);
		FactuurHeader factuurHeader = new FactuurHeader(debiteur, date, "272011");

		Currency currency = Currency.getInstance("EUR");

		ItemList<ReparatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "110543", new Geld(77), new Geld(6.5)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111558", new Geld(77), new Geld(9)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111518", new Geld(57.75), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111660", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111563", new Geld(115.5), new Geld(13.5)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111625", new Geld(57.75), new Geld(15)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111764", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111751", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111745", new Geld(38.5), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111571", new Geld(57.75), new Geld(3.5)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111876", new Geld(77), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111716", new Geld(154), new Geld(7.5)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111854", new Geld(154), new Geld(183.5)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111912", new Geld(38.5), new Geld(9)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111940", new Geld(154), new Geld(9)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111928", new Geld(77), new Geld(4.5)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111723", new Geld(115.5), new Geld(0)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111963", new Geld(299.26), new Geld(448.88)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111739", new Geld(408.16), new Geld(136.52)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111091", new Geld(1451.27), new Geld(967.51)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111409", new Geld(2546.57), new Geld(1697.72)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111272", new Geld(3630.66), new Geld(2420.44)));
		itemList.add(new ReparatiesInkoopOrder("Bonnummer", "111148", new Geld(3878.2), new Geld(2585.46)));

		ReparatiesFactuur factuur = new ReparatiesFactuur(factuurHeader, currency, itemList);
		this.write(factuur, "ReparatiesFactuur.xml");
	}
}
