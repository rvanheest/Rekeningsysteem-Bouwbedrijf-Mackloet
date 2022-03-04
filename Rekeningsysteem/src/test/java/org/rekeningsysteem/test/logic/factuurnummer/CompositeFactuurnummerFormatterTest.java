package org.rekeningsysteem.test.logic.factuurnummer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.logic.factuurnummer.CompositeFactuurnummerFormatter;
import org.rekeningsysteem.logic.factuurnummer.Factuurnummer;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerFormatter;

public class CompositeFactuurnummerFormatterTest {

  private static final String kenmerk = "3";
  private FactuurnummerFormatter formatter;

  @Before
  public void setUp() {
    this.formatter = CompositeFactuurnummerFormatter.Create(kenmerk);
  }

  @Test
  public void newFormatFactuurnummerHeeftJaarTest() {
    Assert.assertTrue(this.formatter.heeftJaar("2020" + kenmerk + "012", "2020"));
  }

  @Test
  public void newFormatFactuurnummerHeeftNietJaarTest() {
    Assert.assertFalse(this.formatter.heeftJaar("2020" + kenmerk + "012", "2021"));
  }

  @Test
  public void oldFormatFactuurnummerHeeftJaarTest() {
    Assert.assertTrue(this.formatter.heeftJaar("122020", "2020"));
  }

  @Test
  public void oldFormatFactuurnummerHeeftNietJaarTest() {
    Assert.assertFalse(this.formatter.heeftJaar("122020", "2021"));
  }

  @Test
  public void newFormatParseFactuurNummerTest() {
    Factuurnummer f = this.formatter.parse("2020" + kenmerk + "012", "2020");
    Assert.assertEquals(12, f.getNummer());
    Assert.assertEquals("2020", f.getJaar());
  }

  @Test
  public void oldFormatParseFactuurNummerTest() {
    Factuurnummer f = this.formatter.parse("122020", "2020");
    Assert.assertEquals(12, f.getNummer());
    Assert.assertEquals("2020", f.getJaar());
  }

  @Test
  public void oldFormatParseOverlappingNummerTest() {
    Factuurnummer f = this.formatter.parse("202020", "2020");
    Assert.assertEquals(20, f.getNummer());
    Assert.assertEquals("2020", f.getJaar());
  }

  @Test
  public void newFormatFormatFactuurnummer() {
    Factuurnummer f = new Factuurnummer("2020", 12);
    Assert.assertEquals("2020" + kenmerk + "012", this.formatter.format(f));
  }

  @Test
  public void oldFormatFormatFactuurnummer() {
    Factuurnummer f = new Factuurnummer("2020", 12);
    Assert.assertEquals("2020" + kenmerk + "012", this.formatter.format(f));
  }
}
