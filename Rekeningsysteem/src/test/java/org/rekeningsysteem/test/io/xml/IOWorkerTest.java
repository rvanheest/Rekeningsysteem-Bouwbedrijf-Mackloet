package org.rekeningsysteem.test.io.xml;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.File;
import java.time.LocalDate;
import java.util.Currency;

import javax.management.modelmbean.XMLParseException;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.pdf.PdfExporter;
import org.rekeningsysteem.io.xml.IOWorker;
import org.rekeningsysteem.io.xml.XmlMaker;
import org.rekeningsysteem.io.xml.XmlReader;
import org.rekeningsysteem.io.xml.XmlReader1;
import org.rekeningsysteem.io.xml.XmlReader2;
import org.rekeningsysteem.io.xml.XmlReader3;

import rx.observers.TestSubscriber;

@RunWith(MockitoJUnitRunner.class)
public class IOWorkerTest {

	private IOWorker loader;
	@Mock private Logger logger;

	@Before
	public void setUp() {
		this.loader = new IOWorker(new XmlMaker(this.logger),
				new PdfExporter(false, this.logger),
				new XmlReader(this.logger),
				new XmlReader1(this.logger),
				new XmlReader2(this.logger),
				new XmlReader3(this.logger),
				this.logger);
	}

	@Test
	public void testLoadEmptyFile() {
		TestSubscriber<AbstractRekening> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\empty.xml"))
				.subscribe(testObserver);

		testObserver.assertNoValues();
		testObserver.assertError(XMLParseException.class);
		testObserver.assertNotCompleted();
		verify(this.logger).error(anyString(), any(XMLParseException.class));
	}

	@Test
	public void testLoadAangenomenFactuur2() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\2Aangenomen.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadAangenomenFactuur3() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\3Aangenomen.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadMutatiesFactuur1() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\1Mutaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(MutatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadMutatiesFactuur2() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\2Mutaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(MutatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadMutatiesFactuur3() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\3Mutaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(MutatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadMutatiesFactuur4() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\4Mutaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(MutatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testSaveMutatiesFactuur() {
		TestSubscriber<AbstractRekening> testObserver = new TestSubscriber<>();

		MutatiesFactuur factuur = this.mutaties();
		File file = new File("src\\test\\resources\\ioWorker\\saveXML\\Mutaties.xml");

		this.loader.save(factuur, file);
		this.loader.load(file).subscribe(testObserver);

		testObserver.assertValue(factuur);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportMutatiesFactuur() {
		MutatiesFactuur factuur = this.mutaties();
		File file = new File("src\\test\\resources\\ioWorker\\savePDF\\Mutaties.pdf");

		this.loader.export(factuur, file);
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadOfferte1() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\1Offerte.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(Offerte.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadOfferte2() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\2Offerte.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(Offerte.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadOfferte3() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\3Offerte.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(Offerte.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadOfferte4() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\4Offerte.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(Offerte.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testSaveOfferte() {
		TestSubscriber<AbstractRekening> testObserver = new TestSubscriber<>();

		Offerte offerte = this.offerte();
		File file = new File("src\\test\\resources\\ioWorker\\saveXML\\Offerte.xml");

		this.loader.save(offerte, file);
		this.loader.load(file).subscribe(testObserver);

		testObserver.assertValue(offerte);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportOfferte() {
		Offerte factuur = this.offerte();
		File file = new File("src\\test\\resources\\ioWorker\\savePDF\\Offerte.pdf");

		this.loader.export(factuur, file);
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadPartFactuur1() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\1Part.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadParticulierFactuur1_1() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\1Particulier1.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadParticulierFactuur1_2() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\1Particulier2.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadParticulierFactuur2() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\2Particulier.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadParticulierFactuur3() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader
				.load(new File("src\\test\\resources\\ioWorker\\loadXML\\3Particulier.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadParticulierFactuur4() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\4Particulier.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ParticulierFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testSaveParticulier() {
		TestSubscriber<AbstractRekening> testObserver = new TestSubscriber<>();

		ParticulierFactuur factuur = this.particulier();
		File file = new File("src\\test\\resources\\ioWorker\\saveXML\\Particulier.xml");

		this.loader.save(factuur, file);
		this.loader.load(file).subscribe(testObserver);

		testObserver.assertValue(factuur);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportParticulierFactuur() {
		ParticulierFactuur factuur = this.particulier();
		File file = new File("src\\test\\resources\\ioWorker\\savePDF\\Particulier.pdf");

		this.loader.export(factuur, file);
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadReparatiesFactuur1() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\1Reparaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ReparatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadReparatiesFactuur2() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\2Reparaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ReparatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadReparatiesFactuur3() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\3Reparaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ReparatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testLoadReparatiesFactuur4() {
		TestSubscriber<Class<? extends AbstractRekening>> testObserver = new TestSubscriber<>();
		this.loader.load(new File("src\\test\\resources\\ioWorker\\loadXML\\4Reparaties.xml"))
				.map(AbstractRekening::getClass)
				.subscribe(testObserver);

		testObserver.assertValue(ReparatiesFactuur.class);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testSaveReparaties() {
		TestSubscriber<AbstractRekening> testObserver = new TestSubscriber<>();

		ReparatiesFactuur factuur = this.reparaties();
		File file = new File("src\\test\\resources\\ioWorker\\saveXML\\Reparaties.xml");

		this.loader.save(factuur, file);
		this.loader.load(file).subscribe(testObserver);

		testObserver.assertValue(factuur);
		testObserver.assertNoErrors();
		testObserver.assertCompleted();
		verifyZeroInteractions(this.logger);
	}

	@Test
	public void testExportReparatiesFactuur() {
		ReparatiesFactuur factuur = this.reparaties();
		File file = new File("src\\test\\resources\\ioWorker\\savePDF\\Reparaties.pdf");

		this.loader.export(factuur, file);
		verifyZeroInteractions(this.logger);
	}

	private MutatiesFactuur mutaties() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
				"Place", "BtwNumber");
		LocalDate datum = LocalDate.of(2011, 5, 9);
		String factuurnummer = "272011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		Currency currency = Currency.getInstance("EUR");

		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("Ordernummer", "111390", new Geld(4971.96)));
		itemList.add(new MutatiesInkoopOrder("Ordernummer", "111477", new Geld(4820.96)));
		itemList.add(new MutatiesInkoopOrder("Ordernummer", "112308", new Geld(5510.74)));

		return new MutatiesFactuur(header, currency, itemList);
	}

	private Offerte offerte() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode", "Place");
		LocalDate datum = LocalDate.of(2011, 8, 11);
		String factuurnummer = "107";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		return new Offerte(header, "foobar", false);
	}

	private ParticulierFactuur particulier() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode", "Place");
		LocalDate datum = LocalDate.of(2011, 4, 2);
		String factuurnummer = "22011";
		String omschrijving = "Voor u verrichte werkzaamheden betreffende renovatie "
				+ "badkamervloer i.v.m. lekkage";
		OmschrFactuurHeader header = new OmschrFactuurHeader(debiteur, datum, factuurnummer,
				omschrijving);

		Currency currency = Currency.getInstance("EUR");

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

		list.add(new ProductLoon("Uurloon à 38.50", 25, new Geld(38.50), 6));
		list.add(new ProductLoon("test123", 12, new Geld(12.50), 6));
		list.add(new InstantLoon("foobar", new Geld(40.00), 6));

		return new ParticulierFactuur(header, currency, list);
	}

	private ReparatiesFactuur reparaties() {
		Debiteur debiteur = new Debiteur("Name", "Street", "Number", "Zipcode",
				"Place", "BtwNumber");
		LocalDate datum = LocalDate.of(2011, 4, 5);
		String factuurnummer = "232011";
		FactuurHeader header = new FactuurHeader(debiteur, datum, factuurnummer);

		Currency currency = Currency.getInstance("EUR");

		ItemList<ReparatiesInkoopOrder> list = new ItemList<>();

		list.add(new ReparatiesInkoopOrder("Ordernummer", "110543", new Geld(77.00), new Geld(6.50)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111558", new Geld(77.00), new Geld(9.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111518", new Geld(57.75), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111660", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111563", new Geld(115.50), new Geld(13.50)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111625", new Geld(57.75), new Geld(15.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111764", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111751", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111745", new Geld(38.50), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111571", new Geld(57.75), new Geld(3.50)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111876", new Geld(77.00), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111716", new Geld(154.00), new Geld(7.50)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111854", new Geld(154.00), new Geld(183.50)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111912", new Geld(38.50), new Geld(9.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111940", new Geld(154.00), new Geld(9.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111928", new Geld(77.00), new Geld(4.50)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111723", new Geld(115.50), new Geld(0.00)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111963", new Geld(299.26), new Geld(448.88)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111739", new Geld(408.16), new Geld(136.52)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111091", new Geld(1451.27), new Geld(967.51)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111409", new Geld(2546.57), new Geld(1697.72)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111272", new Geld(3630.66), new Geld(2420.44)));
		list.add(new ReparatiesInkoopOrder("Ordernummer", "111148", new Geld(3878.20), new Geld(2585.46)));

		return new ReparatiesFactuur(header, currency, list);
	}
}
