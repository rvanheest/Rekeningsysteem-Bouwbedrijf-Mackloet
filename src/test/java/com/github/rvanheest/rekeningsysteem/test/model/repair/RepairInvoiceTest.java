package com.github.rvanheest.rekeningsysteem.test.model.repair;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractInvoice;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitor;
import com.github.rvanheest.rekeningsysteem.test.model.document.AbstractInvoiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RepairInvoiceTest extends AbstractInvoiceTest<RepairListItem> {

  private RepairInvoice invoice;
  @Mock private DocumentVisitor<Object> visitor;
  @Mock private DocumentVoidVisitor voidVisitor;

  @Override
  protected RepairInvoice makeInstance() {
    return (RepairInvoice) super.makeInstance();
  }

  @Override
  protected AbstractInvoice<RepairListItem> makeInstance(Header header, ItemList<RepairListItem> itemList) {
    return new RepairInvoice(header, itemList);
  }

  @Override
  protected RepairInvoice makeNotInstance() {
    return (RepairInvoice) super.makeNotInstance();
  }

  @Override
  protected AbstractInvoice<RepairListItem> makeNotInstance(Header otherHeader, ItemList<RepairListItem> itemList) {
    return new RepairInvoice(otherHeader, itemList);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.invoice = this.makeInstance();
  }

  @Test
  public void testAccept() throws Exception {
    Object obj = new Object();
    when(this.visitor.visit(eq(this.invoice))).thenReturn(obj);

    assertEquals(obj, this.invoice.accept(this.visitor));

    verify(this.visitor).visit(eq(this.invoice));
  }

  @Test
  public void testAcceptVoid() throws Exception {
    this.invoice.accept(this.voidVisitor);

    verify(this.voidVisitor).visit(eq(this.invoice));
  }

  @Test
  public void testToString() {
    String expected = "<RepairInvoice[<Header[<Debtor[Optional.empty, a, b, c, d, e, Optional.empty]>, 1992-07-30, "
        + "Optional.empty]>, itemList]>";
    assertEquals(expected, this.invoice.toString());
  }
}
