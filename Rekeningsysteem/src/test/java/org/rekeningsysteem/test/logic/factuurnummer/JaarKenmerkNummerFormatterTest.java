package org.rekeningsysteem.test.logic.factuurnummer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.logic.factuurnummer.Factuurnummer;
import org.rekeningsysteem.logic.factuurnummer.JaarKenmerkNummerFormatter;

public class JaarKenmerkNummerFormatterTest {

  private static final String kenmerk = "1";
  private JaarKenmerkNummerFormatter formatter;

  @Before
  public void setUp() {
    this.formatter = new JaarKenmerkNummerFormatter(kenmerk);
  }

  @Test
  public void factuurnummerHeeftJaarTest() {
    Assert.assertTrue(this.formatter.heeftJaar("2020" + kenmerk + "012", "2020"));
  }

  @Test
  public void factuurnummerHeeftNietJaarTest() {
    Assert.assertFalse(this.formatter.heeftJaar("2020" + kenmerk + "012", "2021"));
  }

  @Test
  public void parseFactuurNummerTest() {
    Factuurnummer f = this.formatter.parse("2020" + kenmerk + "012", "2020");
    Assert.assertEquals(12, f.getNummer());
    Assert.assertEquals("2020", f.getJaar());
  }

  @Test
  public void formatFactuurnummer() {
    Factuurnummer f = new Factuurnummer("2020", 12);
    Assert.assertEquals("2020" + kenmerk + "012", this.formatter.format(f));
  }
}
