package com.github.rvanheest.rekeningsysteem.test.xml;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader4;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class XmlReader4Test {

  private XmlReader4 reader;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private static Path xml4Dir;

  @BeforeClass
  public static void setUpClass() throws URISyntaxException {
    TestSupportFixture.slfBridger();
    xml4Dir = Paths.get(XmlReader4Test.class.getClassLoader().getResource("xml/xml4").toURI());
  }

  @Before
  public void setUp() throws ParserConfigurationException {
    this.reader = new XmlReader4(DocumentBuilderFactory.newInstance().newDocumentBuilder());
  }

  @Test
  public void testReadMutationInvoice() throws DifferentCurrencyException {
    Debtor debtor = new Debtor("Name", "Street", "Number", "ZipCode", "Place", "btw");
    LocalDate date = LocalDate.of(2011, 5, 9);
    Header factuurHeader = new Header(debtor, date, "272011");

    ItemList<MutationListItem> itemList = new ItemList<>(this.currency);
    itemList.add(new MutationListItem("Bonnummer", "111390", Money.of(4971.96, this.currency)));
    itemList.add(new MutationListItem("Bonnummer", "111477", Money.of(4820.96, this.currency)));
    itemList.add(new MutationListItem("Bonnummer", "112308", Money.of(5510.74, this.currency)));

    this.reader.load(xml4Dir.resolve("MutatiesFactuur.xml"))
        .test()
        .assertValue(new MutationInvoice(factuurHeader, itemList))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadOffer() {
    Debtor debtor = new Debtor("Dhr. M. Stolk", "Ring", "", "", "Nieuwe-Tonge");
    LocalDate date = LocalDate.of(2011, 8, 11);
    Header factuurHeader = new Header(debtor, date, "107");

    String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce quis quam tortor.";

    this.reader.load(xml4Dir.resolve("Offerte.xml"))
        .test()
        .assertValue(new Offer(factuurHeader, text, false))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadNormalInvoice() throws DifferentCurrencyException {
    Debtor debtor = new Debtor("Name", "Street", "Number", "Zipcode", "Place");
    LocalDate date = LocalDate.of(2011, 4, 2);
    Header header = new Header(debtor, date, "22011");
    String description = "Voor u verrichte werkzaamheden betreffende renovatie badkamervloer i.v.m. lekkage";

    ItemList<NormalListItem> itemList = new ItemList<>(this.currency);
    itemList.add(new EsselinkListItem("Product 1", new EsselinkItem("2018021117", "Product 1", 1, "Zak", Money.of(5.16, this.currency)), 8.0, 21.0));
    itemList.add(new EsselinkListItem("Product 2", new EsselinkItem("2003131360", "Product 2", 1, "zak", Money.of(129.53, this.currency)), 1.0, 21.0));
    itemList.add(new EsselinkListItem("Product 3", new EsselinkItem("2003131060", "Product 3", 1, "set", Money.of(35.96, this.currency)), 1.0, 21.0));
    itemList.add(new EsselinkListItem("Product 4", new EsselinkItem("2003131306", "Product 4", 1, "zak", Money.of(9.47, this.currency)), 1.0, 21.0));
    itemList.add(new EsselinkListItem("Product 5", new EsselinkItem("4010272112", "Product 5", 1, "Stuks", Money.of(17.18, this.currency)), 1.0, 21.0));
    itemList.add(new EsselinkListItem("Product 6", new EsselinkItem("2009200131", "Product 6", 1, "Stuks", Money.of(6.84, this.currency)), 1.0, 21.0));
    itemList.add(new EsselinkListItem("Product 7", new EsselinkItem("2009200105", "Product 7", 1, "Stuks", Money.of(7.44, this.currency)), 1.0, 21.0));
    itemList.add(new SimpleListItem("Stucloper + trapfolie", Money.of(15.0, this.currency), 21.0));
    itemList.add(new SimpleListItem("Kitwerk", Money.of(149.5, this.currency), 21.0));
    itemList.add(new HourlyWage("Uurloon à 38.50", 25.0, Money.of(38.5, this.currency), 6.0));
    itemList.add(new HourlyWage("test123", 12.0, Money.of(12.5, this.currency), 6.0));
    itemList.add(new DefaultWage("foobar", Money.of(40.0, this.currency), 6.0));

    this.reader.load(xml4Dir.resolve("ParticulierFactuur.xml"))
        .test()
        .assertValue(new NormalInvoice(header, description, itemList))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadRepairsInvoice() throws DifferentCurrencyException {
    Debtor debtor = new Debtor("Name", "Street", "Number", "Zipcode", "Place", "BtwNumber");
    LocalDate date = LocalDate.of(2011, 4, 5);
    Header header = new Header(debtor, date, "232011");

    ItemList<RepairListItem> itemList = new ItemList<>(this.currency);
    itemList.add(new RepairListItem("Bonnummer", "110543", Money.of(77, this.currency), Money.of(6.5, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111558", Money.of(77, this.currency), Money.of(9, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111518", Money.of(57.75, this.currency), Money.of(0, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111660", Money.of(77, this.currency), Money.of(0, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111563", Money.of(115.5, this.currency), Money.of(13.5, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111625", Money.of(57.75, this.currency), Money.of(15, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111764", Money.of(77, this.currency), Money.of(0, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111751", Money.of(77, this.currency), Money.of(0, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111745", Money.of(38.5, this.currency), Money.of(0, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111571", Money.of(57.75, this.currency), Money.of(3.5, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111876", Money.of(77, this.currency), Money.of(0, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111716", Money.of(154, this.currency), Money.of(7.5, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111854", Money.of(154, this.currency), Money.of(183.5, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111912", Money.of(38.5, this.currency), Money.of(9, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111940", Money.of(154, this.currency), Money.of(9, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111928", Money.of(77, this.currency), Money.of(4.5, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111723", Money.of(115.5, this.currency), Money.of(0, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111963", Money.of(299.26, this.currency), Money.of(448.88, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111739", Money.of(408.16, this.currency), Money.of(136.52, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111091", Money.of(1451.27, this.currency), Money.of(967.51, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111409", Money.of(2546.57, this.currency), Money.of(1697.72, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111272", Money.of(3630.66, this.currency), Money.of(2420.44, this.currency)));
    itemList.add(new RepairListItem("Bonnummer", "111148", Money.of(3878.2, this.currency), Money.of(2585.46, this.currency)));

    this.reader.load(xml4Dir.resolve("ReparatiesFactuur.xml"))
        .test()
        .assertValue(new RepairInvoice(header, itemList))
        .assertNoErrors()
        .assertComplete();
  }
}
