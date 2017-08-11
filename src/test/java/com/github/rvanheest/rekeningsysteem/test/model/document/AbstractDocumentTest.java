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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class AbstractDocumentTest extends EqualsHashCodeTest implements TestSupportFixture {

  private AbstractDocument document;
  private final Header header = new Header(new Debtor("a", "b", "c", "d", "e"), LocalDate.of(1992, 7, 30));

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

  @Test
  public void testInitInvoiceNumber() {
    assertFalse(this.header.getInvoiceNumber().isPresent());
    String expectedInvoiceNumber = "122017";
    this.document.initInvoiceNumber(expectedInvoiceNumber);

    Optional<String> invoiceNumber = this.header.getInvoiceNumber();
    assertTrue(invoiceNumber.isPresent());
    assertEquals(expectedInvoiceNumber, invoiceNumber.get());
  }

  @Test
  public void testInitInvoiceNumberAlreadySet() {
    this.testInitInvoiceNumber();

    assertTrue(this.header.getInvoiceNumber().isPresent());
    this.document.initInvoiceNumber("152017");

    Optional<String> invoiceNumber = this.header.getInvoiceNumber();
    assertTrue(invoiceNumber.isPresent());
    assertEquals("122017", invoiceNumber.get());
  }

  @Test
  public void testEqualsFalseOtherFactuurHeader() {
    assertFalse(this.document.equals(
        this.makeNotInstance(new Header(new Debtor("", "", "", "", ""), LocalDate.of(1992, 7, 30)))));
  }
}
