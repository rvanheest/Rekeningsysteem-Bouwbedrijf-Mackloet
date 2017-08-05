package com.github.rvanheest.rekeningsysteem.test.model.document;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.test.model.EqualsHashCodeTest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public abstract class AbstractDocumentTest extends EqualsHashCodeTest implements TestSupportFixture {

  private AbstractDocument document;
  private final Header header = new Header(new Debtor("a", "b", "c", "d", "e"), LocalDate.of(1992, 7, 30));
  // TODO FactuurnummerManager mock

  @Override
  protected AbstractDocument makeInstance() {
    return this.makeInstance(this.header);
  }

  protected abstract AbstractDocument makeInstance(Header header);

  @Override
  protected AbstractDocument makeNotInstance() {
    return this.makeNotInstance(new Header(new Debtor("", "", "", "", ""), LocalDate.of(1992, 7, 30)));
  }

  protected abstract AbstractDocument makeNotInstance(Header header);

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.document = this.makeInstance();
  }

  @Test
  public void testGetHeader() {
    assertEquals(this.header, this.document.getHeader());
  }

  //@Test
  //public void testInitFactuurnummer() {
  //  assertFalse(this.header.getInvoiceNumber().isPresent());
  //
  //  when(this.factuurnummerManager.getFactuurnummer()).thenReturn("12014");
  //
  //  this.rekening.initFactuurnummer(this.factuurnummerManager);
  //
  //  assertEquals(Optional.of("12014"), this.header.getFactuurnummer());
  //  verify(this.factuurnummerManager).getFactuurnummer();
  //}

  //@Test
  //public void testSameFactuurnummer() {
  //  this.header.setInvoiceNumber("12013");
  //  assertTrue(this.header.getInvoiceNumber().isPresent());
  //
  //  this.rekening.initFactuurnummer(this.factuurnummerManager);
  //
  //  assertEquals(Optional.of("12013"), this.header.getInvoiceNumber());
  //  verifyZeroInteractions(this.factuurnummerManager);
  //}

  @Test
  public void testEqualsFalseOtherFactuurHeader() {
    assertFalse(this.document.equals(
        this.makeNotInstance(new Header(new Debtor("", "", "", "", ""), LocalDate.of(1992, 7, 30)))));
  }
}
