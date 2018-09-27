package com.github.rvanheest.rekeningsysteem.test.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.businesslogic.model.MutationInvoiceManager;
import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
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

public class MutationInvoiceManagerTest extends AbstractDocumentManagerTest<MutationInvoice> {

  private MutationInvoiceManager invoiceManager;

  private final MutationInvoice emptyInvoice = new MutationInvoice(
      new Header(
          new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
          LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
          "12018"
      ),
      new ItemList<>(Monetary.getCurrency("EUR"))
  );

  @Override
  protected MutationInvoice emptyDoc() {
    return this.emptyInvoice;
  }

  @Override
  protected MutationInvoice createDocument(Header header) {
    return new MutationInvoice(
        header,
        this.emptyInvoice.getItemList()
    );
  }

  @Override
  protected MutationInvoiceManager createDocumentManager() {
    return new MutationInvoiceManager(this.emptyInvoice);
  }

  @Before
  @Override
  public void setUp() {
    super.setUp();
    this.invoiceManager = this.createDocumentManager();
  }

  @Test
  public void testAddToItemList() throws DifferentCurrencyException {
    TestObserver<ItemList<MutationListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<MutationInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    MutationListItem newItem1 = new MutationListItem("description1", "itemId1", Money.of(1.23, currency));
    MutationListItem newItem2 = new MutationListItem("description2", "itemId2", Money.of(4.56, currency));
    ItemList<MutationListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<MutationListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    MutationInvoice expectedInvoice1 = new MutationInvoice(expectedHeader, expectedItemList1);
    MutationInvoice expectedInvoice2 = new MutationInvoice(expectedHeader, expectedItemList2);

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
    TestObserver<ItemList<MutationListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<MutationInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency1 = Monetary.getCurrency("EUR");
    CurrencyUnit currency2 = Monetary.getCurrency("USD");
    MutationListItem newItem1 = new MutationListItem("description1", "itemId1", Money.of(1.23, currency1));
    MutationListItem newItem2 = new MutationListItem("description2", "itemId2", Money.of(4.56, currency2));
    ItemList<MutationListItem> expectedItemList1 = new ItemList<>(currency1, Collections.singletonList(newItem1));
    MutationInvoice expectedInvoice1 = new MutationInvoice(expectedHeader, expectedItemList1);

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
    TestObserver<ItemList<MutationListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<MutationInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    MutationListItem newItem1 = new MutationListItem("description1", "itemId1", Money.of(1.23, currency));
    MutationListItem newItem2 = new MutationListItem("description2", "itemId2", Money.of(4.56, currency));
    ItemList<MutationListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<MutationListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    ItemList<MutationListItem> expectedItemList3 = new ItemList<>(currency, Collections.singletonList(newItem2));
    MutationInvoice expectedInvoice1 = new MutationInvoice(expectedHeader, expectedItemList1);
    MutationInvoice expectedInvoice2 = new MutationInvoice(expectedHeader, expectedItemList2);
    MutationInvoice expectedInvoice3 = new MutationInvoice(expectedHeader, expectedItemList3);

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
    TestObserver<ItemList<MutationListItem>> itemListTestObserver = this.invoiceManager.getItemList().skip(1L).test();
    TestObserver<MutationInvoice> invoiceTestObserver = this.invoiceManager.getDocument().skip(1L).test();

    Header expectedHeader = new Header(
        new Debtor(Optional.empty(), "", "", "", "", "", Optional.empty()),
        LocalDate.parse("2018-07-30", DateTimeFormatter.ISO_DATE),
        "12018"
    );
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    MutationListItem newItem1 = new MutationListItem("description1", "itemId1", Money.of(1.23, currency));
    MutationListItem newItem2 = new MutationListItem("description2", "itemId2", Money.of(4.56, currency));
    ItemList<MutationListItem> expectedItemList1 = new ItemList<>(currency, Collections.singletonList(newItem1));
    ItemList<MutationListItem> expectedItemList2 = new ItemList<>(currency, Arrays.asList(newItem1, newItem2));
    ItemList<MutationListItem> expectedItemList3 = new ItemList<>(currency);
    MutationInvoice expectedInvoice1 = new MutationInvoice(expectedHeader, expectedItemList1);
    MutationInvoice expectedInvoice2 = new MutationInvoice(expectedHeader, expectedItemList2);
    MutationInvoice expectedInvoice3 = new MutationInvoice(expectedHeader, expectedItemList3);

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
