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
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.xml.XmlReader1;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Locale;

public class XmlReader1Test {

  private XmlReader1 reader;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private static Path xml1Dir;

  @BeforeClass
  public static void setUpClass() throws URISyntaxException {
    TestSupportFixture.slfBridger();
    xml1Dir = Paths.get(XmlReader1Test.class.getClassLoader().getResource("xml/xml1").toURI());
  }

  @Before
  public void setUp() throws ParserConfigurationException {
    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    this.reader = new XmlReader1(builder, Locale.forLanguageTag("nl-NL"), this.currency);
  }

  @Test
  public void testReadMutationInvoice() throws DifferentCurrencyException {
    Debtor debtor = new Debtor("testnaam", "teststraat", "testnummer", "testpostcode", "testplaats", "testbtwnr");
    LocalDate date = LocalDate.of(2012, 2, 7);
    Header header = new Header(debtor, date, "72012");

    ItemList<MutationListItem> itemList = new ItemList<>(this.currency);
    itemList.add(new MutationListItem("Bonnummer", "13151", Money.of(2135131.00, this.currency)));

    this.reader.load(xml1Dir.resolve("MutatiesFactuur.xml"))
        .test()
        .assertValue(new MutationInvoice(header, itemList))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadOffer() {
    Debtor debtor = new Debtor("testnaam", "teststraat", "testnummer", "testpostcode", "testplaats");
    LocalDate date = LocalDate.of(2012, 8, 24);
    Header header = new Header(debtor, date, "62012");

    this.reader.load(xml1Dir.resolve("Offerte.xml"))
        .test()
        .assertValue(new Offer(header, "dsafsdkljfaskljfpoj", true))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadNormalInvoice() throws DifferentCurrencyException {
    Debtor debtor = new Debtor("testnaam", "teststraat", "testnummer", "testpostcode", "testplaats");
    LocalDate date = LocalDate.of(2012, 1, 2);
    Header header = new Header(debtor, date, "12012");

    ItemList<NormalListItem> itemList = new ItemList<>(this.currency);
    itemList.add(new EsselinkListItem(new EsselinkItem("123456", "testomschr", 1, "stuks", Money.of(1078.80, this.currency)), 12.0, 0.0));
    itemList.add(new EsselinkListItem(new EsselinkItem("456789", "testomschr2", 1, "stuks", Money.of(1078.80, this.currency)), 11.0, 0.0));
    itemList.add(new HourlyWage("Uurloon à 2,50", 1.0, Money.of(2.50, this.currency), 0.0));

    this.reader.load(xml1Dir.resolve("PartFactuur.xml"))
        .test()
        .assertValue(new NormalInvoice(header, "Nieuwjaarstest 2011-2012", itemList))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadNormalInvoice1() throws DifferentCurrencyException {
    Debtor debtor = new Debtor("testnaam", "teststraat", "testnummer", "testpostcode", "testplaats");
    LocalDate date = LocalDate.of(2012, 8, 13);
    Header header = new Header(debtor, date, "302012");

    ItemList<NormalListItem> itemList = new ItemList<>(this.currency);
    itemList.add(new EsselinkListItem(new EsselinkItem("12345", "testomschr", 1, "Zak", Money.of(46.14, this.currency)), 6.0, 19.0));
    itemList.add(new EsselinkListItem(new EsselinkItem("1234", "testomschr2", 1, "zak", Money.of(5.95, this.currency)), 6.0, 19.0));
    itemList.add(new EsselinkListItem(new EsselinkItem("7985464", "testomschr3", 1, "emmer", Money.of(42.32, this.currency)), 20.0, 19.0));
    itemList.add(new SimpleListItem("Ander artikel", Money.of(50.00, this.currency), 19.0));
    itemList.add(new HourlyWage("Uurloon à 5,60", 20.0, Money.of(5.60, this.currency), 19.0));

    this.reader.load(xml1Dir.resolve("ParticulierFactuur1.xml"))
        .test()
        .assertValue(new NormalInvoice(header, "TestOmschrijving", itemList))
        .assertNoErrors()
        .assertComplete();
  }

  @Test
  public void testReadNormalInvoice2() throws DifferentCurrencyException {
    Debtor debtor = new Debtor("testnaam", "teststraat", "testnummer", "testpostcode", "testplaats");
    LocalDate date = LocalDate.of(2013, 2, 23);
    Header header = new Header(debtor, date, "32013");

    ItemList<NormalListItem> itemList = new ItemList<>(this.currency);
    itemList.add(new EsselinkListItem(new EsselinkItem("456123", "testomschr", 1, "zak", Money.of(9.83, this.currency)), 20.0, 21.0));
    itemList.add(new EsselinkListItem(new EsselinkItem("789456123", "testomschr2", 1, "emmer", Money.of(42.32, this.currency)), 6.0, 21.0));
    itemList.add(new SimpleListItem("test", Money.of(20.00, this.currency), 21.0));
    itemList.add(new HourlyWage("Uurloon à 6,50", 3.0, Money.of(6.50, this.currency), 6.0));

    this.reader.load(xml1Dir.resolve("ParticulierFactuur2.xml"))
        .test()
        .assertValue(new NormalInvoice(header, "test123", itemList))
        .assertNoErrors()
        .assertComplete();
  }
}
