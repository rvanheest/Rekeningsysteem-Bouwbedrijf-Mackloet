package com.github.rvanheest.rekeningsysteem.test.model.document;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.AbstractInvoice;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.ListItem;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractInvoiceTest<E extends ListItem> extends AbstractDocumentTest {

  private AbstractInvoice<E> invoice;
  @Mock private ItemList<E> itemList;

  @Override
  @SuppressWarnings("unchecked")
  protected AbstractInvoice<E> makeInstance() {
    return (AbstractInvoice<E>) super.makeInstance();
  }

  @Override
  protected AbstractDocument makeInstance(Header header) {
    return this.makeInstance(header, this.itemList);
  }

  protected abstract AbstractInvoice<E> makeInstance(Header header, ItemList<E> itemList);

  @Override
  @SuppressWarnings("unchecked")
  protected AbstractInvoice<E> makeNotInstance() {
    return (AbstractInvoice<E>) super.makeNotInstance();
  }

  @Override
  protected AbstractDocument makeNotInstance(Header otherHeader) {
    return this.makeNotInstance(otherHeader, this.itemList);
  }

  protected abstract AbstractInvoice<E> makeNotInstance(Header otherHeader, ItemList<E> itemList);

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.invoice = this.makeInstance();
  }

  @Test
  public void testGetItemList() {
    assertEquals(this.itemList, this.invoice.getItemList());
  }

  @Test
  public void testGetTotals() {
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    Totals expected = new Totals(currency);
    when(this.itemList.getTotals()).thenReturn(expected);

    assertEquals(expected, this.invoice.getTotals());
    verify(this.itemList).getTotals();
  }

  @Test
  public void testEqualsFalseOtherItemList() {
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    ItemList<E> otherList = new ItemList<>(currency);
    assertFalse(this.invoice.equals(this.makeInstance(this.invoice.getHeader(), otherList)));
  }
}
