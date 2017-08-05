package com.github.rvanheest.rekeningsysteem.test.model.offer;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVisitor;
import com.github.rvanheest.rekeningsysteem.model.visitor.DocumentVoidVisitor;
import com.github.rvanheest.rekeningsysteem.test.model.document.AbstractDocumentTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OfferTest extends AbstractDocumentTest {

  private Offer offer;
  private final String text = "Lorem ipsum dolor sit amet.";
  @Mock private DocumentVisitor<Object> visitor;
  @Mock private DocumentVoidVisitor voidVisitor;

  @Override
  protected Offer makeInstance() {
    return (Offer) super.makeInstance();
  }

  @Override
  protected Offer makeInstance(Header header) {
    return new Offer(header, this.text, true);
  }

  @Override
  protected Offer makeNotInstance() {
    return (Offer) super.makeNotInstance();
  }

  @Override
  protected AbstractDocument makeNotInstance(Header otherHeader) {
    return new Offer(otherHeader, this.text + "x", true);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.offer = this.makeInstance();
  }

  @Test
  public void testGetText() {
    assertEquals(this.text, this.offer.getText());
  }

  @Test
  public void testIsSign() {
    assertTrue(this.offer.isSign());
  }

  @Test
  public void testAccept() throws Exception {
    Object obj = new Object();
    when(this.visitor.visit(eq(this.offer))).thenReturn(obj);

    assertEquals(obj, this.offer.accept(this.visitor));

    verify(this.visitor).visit(eq(this.offer));
  }

  @Test
  public void testAcceptVoid() throws Exception {
    this.offer.accept(this.voidVisitor);

    verify(this.voidVisitor).visit(eq(this.offer));
  }

  @Test
  public void testEqualsFalseOtherText() {
    Offer off = new Offer(this.offer.getHeader(), this.text + ".", true);
    assertFalse(this.offer.equals(off));
  }

  @Test
  public void testEqualsFalseOtherSign() {
    Offer off = new Offer(this.offer.getHeader(), this.text, false);
    assertFalse(this.offer.equals(off));
  }

  @Test
  public void testToString() {
    assertEquals("<Offer[<Header[<Debtor[Optional.empty, a, b, c, d, e, Optional.empty]>, 1992-07-30, "
            + "Optional.empty]>, Lorem ipsum dolor sit amet., true]>", this.offer.toString());
  }
}
