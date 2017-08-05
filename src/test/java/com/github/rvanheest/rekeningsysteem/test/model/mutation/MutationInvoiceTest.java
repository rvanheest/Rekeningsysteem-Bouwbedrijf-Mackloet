package com.github.rvanheest.rekeningsysteem.test.model.mutation;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
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
public class MutationInvoiceTest extends AbstractInvoiceTest<MutationListItem> {

  private MutationInvoice invoice;
  @Mock private DocumentVisitor<Object> visitor;
  @Mock private DocumentVoidVisitor voidVisitor;

  @Override
  protected MutationInvoice makeInstance() {
    return (MutationInvoice) super.makeInstance();
  }

  @Override
  protected MutationInvoice makeInstance(Header header, ItemList<MutationListItem> itemList) {
    return new MutationInvoice(header, itemList);
  }

  @Override
  protected MutationInvoice makeNotInstance() {
    return (MutationInvoice) super.makeNotInstance();
  }

  @Override
  protected MutationInvoice makeNotInstance(Header otherHeader, ItemList<MutationListItem> itemList) {
    return new MutationInvoice(otherHeader, itemList);
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
    String expected = "<MutationInvoice[<Header[<Debtor[Optional.empty, a, b, c, d, e, Optional.empty]>, 1992-07-30, "
        + "Optional.empty]>, itemList]>";
    assertEquals(expected, this.invoice.toString());
  }
}
