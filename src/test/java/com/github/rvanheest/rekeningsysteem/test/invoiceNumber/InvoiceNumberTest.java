package com.github.rvanheest.rekeningsysteem.test.invoiceNumber;

import com.github.rvanheest.rekeningsysteem.invoiceNumber.InvoiceNumber;
import com.github.rvanheest.rekeningsysteem.test.model.EqualsHashCodeTest;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InvoiceNumberTest extends EqualsHashCodeTest {

  private InvoiceNumber invoiceNumber;
  private final int number = 16;
  private final int year = 2017;

  @Override
  protected InvoiceNumber makeInstance() {
    return new InvoiceNumber(this.number, this.year);
  }

  @Override
  protected InvoiceNumber makeNotInstance() {
    return new InvoiceNumber(this.number + 1, this.year);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.invoiceNumber = this.makeInstance();
  }

  @Test
  public void testGetNumber() {
    assertEquals(this.number, this.invoiceNumber.getNumber());
  }

  @Test
  public void testGetYear() {
    assertEquals(this.year, this.invoiceNumber.getYear());
  }

  @Test
  public void testFormat() {
    assertEquals("162017", this.invoiceNumber.formatted());
  }

  @Test
  public void testEqualsOtherNumber() {
    InvoiceNumber other = new InvoiceNumber(this.number + 1, this.year);
    assertFalse(this.invoiceNumber.equals(other));
  }

  @Test
  public void testEqualsOtherYear() {
    InvoiceNumber other = new InvoiceNumber(this.number, this.year + 1);
    assertFalse(this.invoiceNumber.equals(other));
  }

  @Test
  public void testToString() {
    assertEquals("<InvoiceNumber[16, 2017]>", this.invoiceNumber.toString());
  }
}
