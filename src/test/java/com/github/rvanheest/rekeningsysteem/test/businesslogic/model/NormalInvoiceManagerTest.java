package com.github.rvanheest.rekeningsysteem.test.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.NormalInvoiceManager;
import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import io.reactivex.observers.TestObserver;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class NormalInvoiceManagerTest extends AbstractDocumentManagerTest<NormalInvoice> {

  private NormalInvoiceManager invoiceManager;

  private final NormalInvoice emptyInvoice = new NormalInvoice(
      new Header(
          new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
          LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
          "12018"
      ),
      "",
      new ItemList<>(Monetary.getCurrency("EUR"))
  );

  @Override
  protected NormalInvoice emptyDoc() {
    return this.emptyInvoice;
  }

  @Override
  protected NormalInvoice createDocument(Header header) {
    return new NormalInvoice(
        header,
        this.emptyInvoice.getDescription(),
        this.emptyInvoice.getItemList()
    );
  }

  @Override
  protected NormalInvoiceManager createDocumentManager() {
    return new NormalInvoiceManager(this.emptyInvoice);
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    this.invoiceManager = this.createDocumentManager();
  }

  @Test
  public void testWithDescription() {
    TestObserver<String> descriptionTestObserver = this.invoiceManager.getDescription().skip(1L).test();
    TestObserver<NormalInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    String expectedDescription = "my-description";
    ItemList<NormalListItem> expectedItemList = new ItemList<>(Monetary.getCurrency("EUR"));
    NormalInvoice expectedInvoice = new NormalInvoice(
        expectedHeader,
        expectedDescription,
        expectedItemList
    );

    this.invoiceManager.withDescription(expectedDescription)
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    descriptionTestObserver
        .assertValue(expectedDescription)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValue(expectedInvoice)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testAddToItemList() throws DifferentCurrencyException {
    TestObserver<ItemList<NormalListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<NormalInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    NormalListItem newItem1 = new EsselinkListItem(
        new EsselinkItem("itemId", "description", 12, "unit", Money.of(5.16, currency)), 12.3, 21.0);
    NormalListItem newItem2 = new SimpleListItem("description", Money.of(1.23, currency), 4.56);
    NormalListItem newItem3 = new DefaultWage("description", Money.of(1.23, currency), 4.56);
    NormalListItem newItem4 = new HourlyWage("description", 7.89, Money.of(1.23, currency), 4.56);
    ItemList<NormalListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<NormalListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    ItemList<NormalListItem> expectedItemList3 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2, newItem3));
    ItemList<NormalListItem> expectedItemList4 = new ItemList<>(currency,
        Arrays.asList(newItem1, newItem2, newItem3, newItem4));
    NormalInvoice expectedInvoice1 = new NormalInvoice(expectedHeader, "", expectedItemList1);
    NormalInvoice expectedInvoice2 = new NormalInvoice(expectedHeader, "", expectedItemList2);
    NormalInvoice expectedInvoice3 = new NormalInvoice(expectedHeader, "", expectedItemList3);
    NormalInvoice expectedInvoice4 = new NormalInvoice(expectedHeader, "", expectedItemList4);

    this.invoiceManager.addToItemList(newItem1)
        .andThen(this.invoiceManager.addToItemList(newItem2))
        .andThen(this.invoiceManager.addToItemList(newItem3))
        .andThen(this.invoiceManager.addToItemList(newItem4))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    itemListTestObserver
        .assertValues(expectedItemList1, expectedItemList2, expectedItemList3, expectedItemList4)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValues(expectedInvoice1, expectedInvoice2, expectedInvoice3, expectedInvoice4)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testAddToItemListWithDifferentCurrency() throws DifferentCurrencyException {
    TestObserver<ItemList<NormalListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<NormalInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency1 = Monetary.getCurrency("EUR");
    CurrencyUnit currency2 = Monetary.getCurrency("USD");
    NormalListItem newItem1 = new EsselinkListItem(
        new EsselinkItem("itemId", "description", 12, "unit", Money.of(5.16, currency1)), 12.3, 21.0);
    NormalListItem newItem2 = new SimpleListItem("description", Money.of(1.23, currency2), 4.56);
    ItemList<NormalListItem> expectedItemList1 = new ItemList<>(currency1, Collections.singletonList(newItem1));
    NormalInvoice expectedInvoice1 = new NormalInvoice(expectedHeader, "", expectedItemList1);

    this.invoiceManager.addToItemList(newItem1)
        .andThen(this.invoiceManager.addToItemList(newItem2))
        .test()
        .assertNoValues()
        .assertError(DifferentCurrencyException.class)
        .assertNotComplete();

    itemListTestObserver
        .assertValue(expectedItemList1)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValues(expectedInvoice1)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testRemoveFromItemList() throws DifferentCurrencyException {
    TestObserver<ItemList<NormalListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<NormalInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    NormalListItem newItem1 = new EsselinkListItem(
        new EsselinkItem("itemId", "description", 12, "unit", Money.of(5.16, currency)), 12.3, 21.0);
    NormalListItem newItem2 = new SimpleListItem("description", Money.of(1.23, currency), 4.56);
    NormalListItem newItem3 = new DefaultWage("description", Money.of(1.23, currency), 4.56);
    NormalListItem newItem4 = new HourlyWage("description", 7.89, Money.of(1.23, currency), 4.56);
    ItemList<NormalListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<NormalListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    ItemList<NormalListItem> expectedItemList3 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2, newItem3));
    ItemList<NormalListItem> expectedItemList4 = new ItemList<>(currency,
        Arrays.asList(newItem1, newItem2, newItem3, newItem4));
    ItemList<NormalListItem> expectedItemList5 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2, newItem4));
    NormalInvoice expectedInvoice1 = new NormalInvoice(expectedHeader, "", expectedItemList1);
    NormalInvoice expectedInvoice2 = new NormalInvoice(expectedHeader, "", expectedItemList2);
    NormalInvoice expectedInvoice3 = new NormalInvoice(expectedHeader, "", expectedItemList3);
    NormalInvoice expectedInvoice4 = new NormalInvoice(expectedHeader, "", expectedItemList4);
    NormalInvoice expectedInvoice5 = new NormalInvoice(expectedHeader, "", expectedItemList5);

    this.invoiceManager.addToItemList(newItem1)
        .andThen(this.invoiceManager.addToItemList(newItem2))
        .andThen(this.invoiceManager.addToItemList(newItem3))
        .andThen(this.invoiceManager.addToItemList(newItem4))
        .andThen(this.invoiceManager.removeFromItemList(newItem3))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    itemListTestObserver
        .assertValues(expectedItemList1, expectedItemList2, expectedItemList3, expectedItemList4, expectedItemList5)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValues(expectedInvoice1, expectedInvoice2, expectedInvoice3, expectedInvoice4, expectedInvoice5)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testClearItemList() throws DifferentCurrencyException {
    TestObserver<ItemList<NormalListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<NormalInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    NormalListItem newItem1 = new EsselinkListItem(
        new EsselinkItem("itemId", "description", 12, "unit", Money.of(5.16, currency)), 12.3, 21.0);
    NormalListItem newItem2 = new SimpleListItem("description", Money.of(1.23, currency), 4.56);
    NormalListItem newItem3 = new DefaultWage("description", Money.of(1.23, currency), 4.56);
    NormalListItem newItem4 = new HourlyWage("description", 7.89, Money.of(1.23, currency), 4.56);
    ItemList<NormalListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<NormalListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    ItemList<NormalListItem> expectedItemList3 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2, newItem3));
    ItemList<NormalListItem> expectedItemList4 = new ItemList<>(currency,
        Arrays.asList(newItem1, newItem2, newItem3, newItem4));
    ItemList<NormalListItem> expectedItemList5 = new ItemList<>(currency);
    NormalInvoice expectedInvoice1 = new NormalInvoice(expectedHeader, "", expectedItemList1);
    NormalInvoice expectedInvoice2 = new NormalInvoice(expectedHeader, "", expectedItemList2);
    NormalInvoice expectedInvoice3 = new NormalInvoice(expectedHeader, "", expectedItemList3);
    NormalInvoice expectedInvoice4 = new NormalInvoice(expectedHeader, "", expectedItemList4);
    NormalInvoice expectedInvoice5 = new NormalInvoice(expectedHeader, "", expectedItemList5);

    this.invoiceManager.addToItemList(newItem1)
        .andThen(this.invoiceManager.addToItemList(newItem2))
        .andThen(this.invoiceManager.addToItemList(newItem3))
        .andThen(this.invoiceManager.addToItemList(newItem4))
        .andThen(this.invoiceManager.clearItemList())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    itemListTestObserver
        .assertValues(expectedItemList1, expectedItemList2, expectedItemList3, expectedItemList4, expectedItemList5)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValues(expectedInvoice1, expectedInvoice2, expectedInvoice3, expectedInvoice4, expectedInvoice5)
        .assertNoErrors()
        .assertNotComplete();
  }
}
