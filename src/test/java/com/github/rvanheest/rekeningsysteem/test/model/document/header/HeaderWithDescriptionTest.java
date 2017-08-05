package com.github.rvanheest.rekeningsysteem.test.model.document.header;

import com.github.rvanheest.rekeningsysteem.model.document.header.HeaderWithDescription;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class HeaderWithDescriptionTest extends HeaderTest {

  private final String description = "not so random description";

  @Override
  protected HeaderWithDescription makeInstance() {
    return new HeaderWithDescription(this.getTestDebtor(), this.getTestDatum(),
        this.getTestFatuurnummer(), this.description);
  }

  @Override
  protected HeaderWithDescription makeNotInstance() {
    return new HeaderWithDescription(this.getTestDebtor(), this.getTestDatum(),
        this.getTestFatuurnummer() + ".", this.description);
  }

  @Override
  protected HeaderWithDescription getInstance() {
    return (HeaderWithDescription) super.getInstance();
  }

  @Test
  public void testGetOmschrijving() {
    assertEquals(this.description, this.getInstance().getOmschrijving());
  }

  @Test
  public void testSecondConstructorWithOmschrijving() {
    HeaderWithDescription header = new HeaderWithDescription(this.getInstance().getDebtor(), this
        .getInstance().getDatum(), this.description);

    assertEquals(this.getInstance().getDebtor(), header.getDebtor());
    assertEquals(this.getInstance().getDatum(), header.getDatum());
    assertEquals(Optional.empty(), header.getInvoiceNumber());
    assertEquals(this.description, header.getOmschrijving());
  }

  @Test
  public void testThirdConstructorWithOptionalFactuurnummer() {
    HeaderWithDescription header = new HeaderWithDescription(this.getInstance().getDebtor(),
        this.getInstance().getDatum(), this.getInstance().getInvoiceNumber(), this.description);

    assertEquals(this.getInstance().getDebtor(), header.getDebtor());
    assertEquals(this.getInstance().getDatum(), header.getDatum());
    assertEquals(Optional.of("32013"), header.getInvoiceNumber());
    assertEquals(this.description, header.getOmschrijving());
  }

  @Test
  public void testEqualsFalseOtherOmschrijving() {
    assertFalse(this.getInstance().equals(
        new HeaderWithDescription(this.getTestDebtor(), this.getTestDatum(),
            this.getTestFatuurnummer(), this.description + ".")));
  }

  @Test
  @Override
  public void testToString() {
    assertEquals("<Header[<Debtor[Optional.empty, xxx, yyy, 112, 1234AB, zzz, Optional.empty]>, " + this.getTestDatum().toString() + ", Optional[32013], "
        + "not so random description]>", this.getInstance().toString());
  }
}
