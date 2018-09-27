package com.github.rvanheest.rekeningsysteem.test.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.RepairInvoiceManager;
import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
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

public class RepairInvoiceManagerTest extends AbstractDocumentManagerTest<RepairInvoice> {

  private RepairInvoiceManager invoiceManager;

  private final RepairInvoice emptyInvoice = new RepairInvoice(
      new Header(
          new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
          LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
          "12018"
      ),
      new ItemList<>(Monetary.getCurrency("EUR"))
  );

  @Override
  protected RepairInvoice emptyDoc() {
    return this.emptyInvoice;
  }

  @Override
  protected RepairInvoice createDocument(Header header) {
    return new RepairInvoice(
        header,
        this.emptyInvoice.getItemList()
    );
  }

  @Override
  protected RepairInvoiceManager createDocumentManager() {
    return new RepairInvoiceManager(this.emptyInvoice);
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    this.invoiceManager = this.createDocumentManager();
  }

  @Test
  public void testAddToItemList() throws DifferentCurrencyException {
    TestObserver<ItemList<RepairListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<RepairInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    RepairListItem newItem1 = new RepairListItem("description1", "itemId1", Money.of(1.23, currency),
        Money.of(4.56, currency));
    RepairListItem newItem2 = new RepairListItem("description2", "itemId2", Money.of(4.56, currency),
        Money.of(7.89, currency));
    ItemList<RepairListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<RepairListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    RepairInvoice expectedInvoice1 = new RepairInvoice(expectedHeader, expectedItemList1);
    RepairInvoice expectedInvoice2 = new RepairInvoice(expectedHeader, expectedItemList2);

    this.invoiceManager.addToItemList(newItem1)
        .andThen(this.invoiceManager.addToItemList(newItem2))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    itemListTestObserver
        .assertValues(expectedItemList1, expectedItemList2)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValues(expectedInvoice1, expectedInvoice2)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testAddToItemListWithDifferentCurrency() throws DifferentCurrencyException {
    TestObserver<ItemList<RepairListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<RepairInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency1 = Monetary.getCurrency("EUR");
    CurrencyUnit currency2 = Monetary.getCurrency("USD");
    RepairListItem newItem1 = new RepairListItem("description1", "itemId1", Money.of(1.23, currency1),
        Money.of(4.56, currency1));
    RepairListItem newItem2 = new RepairListItem("description2", "itemId2", Money.of(4.56, currency2),
        Money.of(7.89, currency2));
    ItemList<RepairListItem> expectedItemList1 = new ItemList<>(currency1, Collections.singletonList(newItem1));
    RepairInvoice expectedInvoice1 = new RepairInvoice(expectedHeader, expectedItemList1);

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
    TestObserver<ItemList<RepairListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<RepairInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    RepairListItem newItem1 = new RepairListItem("description1", "itemId1", Money.of(1.23, currency),
        Money.of(4.56, currency));
    RepairListItem newItem2 = new RepairListItem("description2", "itemId2", Money.of(4.56, currency),
        Money.of(7.89, currency));
    ItemList<RepairListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<RepairListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    ItemList<RepairListItem> expectedItemList3 = new ItemList<>(currency, Collections.singletonList(newItem2));
    RepairInvoice expectedInvoice1 = new RepairInvoice(expectedHeader, expectedItemList1);
    RepairInvoice expectedInvoice2 = new RepairInvoice(expectedHeader, expectedItemList2);
    RepairInvoice expectedInvoice3 = new RepairInvoice(expectedHeader, expectedItemList3);

    this.invoiceManager.addToItemList(newItem1)
        .andThen(this.invoiceManager.addToItemList(newItem2))
        .andThen(this.invoiceManager.removeFromItemList(newItem1))
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    itemListTestObserver
        .assertValues(expectedItemList1, expectedItemList2, expectedItemList3)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValues(expectedInvoice1, expectedInvoice2, expectedInvoice3)
        .assertNoErrors()
        .assertNotComplete();
  }

  @Test
  public void testClearItemList() throws DifferentCurrencyException {
    TestObserver<ItemList<RepairListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<RepairInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    RepairListItem newItem1 = new RepairListItem("description1", "itemId1", Money.of(1.23, currency),
        Money.of(4.56, currency));
    RepairListItem newItem2 = new RepairListItem("description2", "itemId2", Money.of(4.56, currency),
        Money.of(7.89, currency));
    ItemList<RepairListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<RepairListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    ItemList<RepairListItem> expectedItemList3 = new ItemList<>(currency);
    RepairInvoice expectedInvoice1 = new RepairInvoice(expectedHeader, expectedItemList1);
    RepairInvoice expectedInvoice2 = new RepairInvoice(expectedHeader, expectedItemList2);
    RepairInvoice expectedInvoice3 = new RepairInvoice(expectedHeader, expectedItemList3);

    this.invoiceManager.addToItemList(newItem1)
        .andThen(this.invoiceManager.addToItemList(newItem2))
        .andThen(this.invoiceManager.clearItemList())
        .test()
        .assertNoValues()
        .assertNoErrors()
        .assertComplete();

    itemListTestObserver
        .assertValues(expectedItemList1, expectedItemList2, expectedItemList3)
        .assertNoErrors()
        .assertNotComplete();

    invoiceTestObserver
        .assertValues(expectedInvoice1, expectedInvoice2, expectedInvoice3)
        .assertNoErrors()
        .assertNotComplete();
  }
}
