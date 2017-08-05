package com.github.rvanheest.rekeningsysteem.test.model.normal;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitor;
import com.github.rvanheest.rekeningsysteem.test.model.document.AbstractInvoiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NormalInvoiceTest extends AbstractInvoiceTest<NormalListItem> {

  private NormalInvoice invoice;
  private final String description = "descr";
  @Mock private DocumentVisitor<Object> visitor;
  @Mock private DocumentVoidVisitor voidVisitor;

  @Override
  protected NormalInvoice makeInstance() {
    return (NormalInvoice) super.makeInstance();
  }

  @Override
  protected NormalInvoice makeInstance(Header header, ItemList<NormalListItem> itemList) {
    return new NormalInvoice(header, this.description, itemList);
  }

  @Override
  protected NormalInvoice makeNotInstance() {
    return (NormalInvoice) super.makeNotInstance();
  }

  @Override
  protected NormalInvoice makeNotInstance(Header otherHeader, ItemList<NormalListItem> itemList) {
    return new NormalInvoice(otherHeader, this.description, itemList);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.invoice = this.makeInstance();
  }

  // TODO test initFactuurnummer

  @Test
  public void testGetDescription() {
    assertEquals(this.description, this.invoice.getDescription());
  }

  @Test
  @Override
  public void testGetTotals() {
    CurrencyUnit currency = Monetary.getCurrency("EUR");
    Totals t1 = new Totals(currency);
    Totals expected = new Totals(currency);
    when(this.invoice.getItemList().getTotals()).thenReturn(t1);

    assertEquals(expected, this.invoice.getTotals());
    verify(this.invoice.getItemList()).getTotals();
  }

  @Test
  public void testAccept() throws Exception {
    this.invoice.accept(this.visitor);

    verify(this.visitor).visit(eq(this.invoice));
  }

  @Test
  public void testAcceptVoid() throws Exception {
    this.invoice.accept(this.voidVisitor);

    verify(this.voidVisitor).visit(eq(this.invoice));
  }

  @Test
  public void testToString() {
    assertEquals("<NormalInvoice[<Header[<Debtor[Optional.empty, a, b, c, d, e, Optional.empty]>, 1992-07-30, "
        + "Optional.empty]>, descr, itemList]>", this.invoice.toString());
  }
}
