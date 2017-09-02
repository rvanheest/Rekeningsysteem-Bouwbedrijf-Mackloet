package com.github.rvanheest.rekeningsysteem.test.pdf;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.pdf.PdfTemplateVisitor;
import com.github.rvanheest.rekeningsysteem.test.ConfigurationFixture;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.apache.commons.configuration.ConfigurationException;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class PdfTemplateVisitorTest implements ConfigurationFixture {

  private PdfTemplateVisitor visitor;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() throws IOException, URISyntaxException, ConfigurationException {
    this.resetTestDir();
    this.visitor = new PdfTemplateVisitor(this.getConfiguration());
  }

  @Test
  public void testVisitMutationInvoice() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city", "vatNumber");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    MutationListItem item1 = new MutationListItem("descr1", "itemId1", Money.of(12.34, this.currency));
    MutationListItem item2 = new MutationListItem("descr2", "itemId2", Money.of(23.45, this.currency));
    MutationListItem item3 = new MutationListItem("descr3", "itemId3", Money.of(34.56, this.currency));
    ItemList<MutationListItem> itemList = new ItemList<>(this.currency, Arrays.asList(item1, item2, item3));
    MutationInvoice invoice = new MutationInvoice(header, itemList);

    Path root = Paths.get(getClass().getClassLoader().getResource("").toURI());
    Path expected = Paths.get(getClass().getClassLoader().getResource("pdftemplate/MutatiesFactuur.tex").toURI());
    assertEquals(root.relativize(expected), this.visitor.visit(invoice));
  }

  @Test
  public void testVisitOffer() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    Offer offer = new Offer(header, "abc", true);

    Path root = Paths.get(getClass().getClassLoader().getResource("").toURI());
    Path expected = Paths.get(getClass().getClassLoader().getResource("pdftemplate/Offerte.tex").toURI());
    assertEquals(root.relativize(expected), this.visitor.visit(offer));
  }

  @Test
  public void testVisitNormalInvoice() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city", "vatNumber");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    SimpleListItem item1 = new SimpleListItem("descr1", Money.of(12.34, this.currency), 25);
    DefaultWage item2 = new DefaultWage("descr2", Money.of(23.45, this.currency), 16);
    HourlyWage item3 = new HourlyWage("descr3", 3.14, Money.of(34.56, this.currency), 16);
    ItemList<NormalListItem> itemList = new ItemList<>(this.currency, Arrays.asList(item1, item2, item3));
    NormalInvoice invoice = new NormalInvoice(header, "invoice descr", itemList);

    Path root = Paths.get(getClass().getClassLoader().getResource("").toURI());
    Path expected = Paths.get(getClass().getClassLoader().getResource("pdftemplate/ParticulierFactuur.tex").toURI());
    assertEquals(root.relativize(expected), this.visitor.visit(invoice));
  }

  @Test
  public void testVisitRepairInvoice() throws Exception {
    Debtor debtor = new Debtor(123, "name", "street", "number", "zipCode", "city", "vatNumber");
    Header header = new Header(debtor, LocalDate.of(2017, 7, 30), "invoiceNumber");
    RepairListItem item1 = new RepairListItem("descr1", "itemId1", Money.of(12.34, this.currency),
        Money.of(96, this.currency));
    RepairListItem item2 = new RepairListItem("descr2", "itemId2", Money.of(23.45, this.currency),
        Money.of(97, this.currency));
    RepairListItem item3 = new RepairListItem("descr3", "itemId3", Money.of(34.56, this.currency),
        Money.of(98, this.currency));
    ItemList<RepairListItem> itemList = new ItemList<>(this.currency, Arrays.asList(item1, item2, item3));
    RepairInvoice invoice = new RepairInvoice(header, itemList);

    Path root = Paths.get(getClass().getClassLoader().getResource("").toURI());
    Path expected = Paths.get(getClass().getClassLoader().getResource("pdftemplate/ReparatiesFactuur.tex").toURI());
    assertEquals(root.relativize(expected), this.visitor.visit(invoice));
  }
}
