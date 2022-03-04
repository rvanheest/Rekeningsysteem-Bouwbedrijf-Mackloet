package org.rekeningsysteem.test.logic.factuurnummer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.logic.factuurnummer.Factuurnummer;
import org.rekeningsysteem.logic.factuurnummer.NummerJaarFormatter;

public class NummerJaarFormatterTest {

  private NummerJaarFormatter formatter;
  
  @Before
  public void setUp() {
    this.formatter = new NummerJaarFormatter();
  }
  
  @Test
  public void factuurnummerHeeftJaarTest() {
    Assert.assertTrue(this.formatter.heeftJaar("122020", "2020"));
  }
  
  @Test
  public void factuurnummerHeeftNietJaarTest() {
    Assert.assertFalse(this.formatter.heeftJaar("122020", "2021"));
  }
  
  @Test
  public void parseFactuurNummerTest() {
    Factuurnummer f = this.formatter.parse("122020", "2020");
    Assert.assertEquals(12, f.getNummer());
    Assert.assertEquals("2020", f.getJaar());
  }
  
  @Test
  public void parseOverlappingNummerTest() {
    Factuurnummer f = this.formatter.parse("202020", "2020");
    Assert.assertEquals(20, f.getNummer());
    Assert.assertEquals("2020", f.getJaar());
  }
  
  @Test
  public void formatFactuurnummer() {
    Factuurnummer f = new Factuurnummer("2020", 12);
    Assert.assertEquals("122020", this.formatter.format(f));
  }
}
