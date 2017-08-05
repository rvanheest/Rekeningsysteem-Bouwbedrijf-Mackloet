package com.github.rvanheest.rekeningsysteem.test.model.document.header;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.test.model.EqualsHashCodeTest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class HeaderTest extends EqualsHashCodeTest {

  private Header header;
  private final Debtor debtor = new Debtor("xxx", "yyy", "112", "1234AB", "zzz");
  private final LocalDate datum = LocalDate.now();
  private final String factuurnummer = "32013";

  @Override
  protected Header makeInstance() {
    return new Header(this.debtor, this.datum, this.factuurnummer);
  }

  @Override
  protected Header makeNotInstance() {
    return new Header(this.debtor, this.datum, this.factuurnummer + ".");
  }

  protected Header getInstance() {
    return this.header;
  }

  protected Debtor getTestDebtor() {
    return this.debtor;
  }

  protected LocalDate getTestDatum() {
    return this.datum;
  }

  protected String getTestFatuurnummer() {
    return this.factuurnummer;
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    this.header = this.makeInstance();
  }

  @Test
  public void testGetDebtor() {
    assertEquals(this.debtor, this.header.getDebtor());
  }

  @Test
  public void testGetDatum() {
    assertEquals(this.datum, this.header.getDatum());
  }

  @Test
  public void testGetFactuurnummer() {
    assertEquals(Optional.of(this.factuurnummer), this.header.getInvoiceNumber());
  }

  @Test
  public void testSetFactuurnummer() {
    this.header.setInvoiceNumber("202020");
    assertEquals(Optional.of("202020"), this.header.getInvoiceNumber());
  }

  @Test
  public void testSecondConstructor() {
    this.header = new Header(this.debtor, this.datum);

    assertEquals(this.debtor, this.header.getDebtor());
    assertEquals(this.datum, this.header.getDatum());
    assertEquals(Optional.empty(), this.header.getInvoiceNumber());
  }

  @Test
  public void testEqualsFalseOtherDebtor() {
    Debtor debiteur2 = new Debtor("RvH", "PB", "116", "3241TA", "MH", "20");
    assertFalse(this.header.equals(new Header(debiteur2, this.datum, this.factuurnummer)));
  }

  @Test
  public void testEqualsFalseOtherDatum() {
    LocalDate datum2 = LocalDate.of(1992, 7, 31);
    assertFalse(this.header.equals(new Header(this.debtor, datum2, this.factuurnummer)));
  }

  @Test
  public void testEqualsFalseOtherFactuurnummer() {
    assertFalse(this.header.equals(new Header(this.debtor, this.datum, this.factuurnummer + ".")));
  }

  @Test
  public void testToString() {
    assertEquals(
        "<Header[<Debtor[Optional.empty, xxx, yyy, 112, 1234AB, zzz, " + "Optional.empty]>, " + this.datum.toString()
            + ", Optional[32013]]>", this.header.toString());
  }
}
