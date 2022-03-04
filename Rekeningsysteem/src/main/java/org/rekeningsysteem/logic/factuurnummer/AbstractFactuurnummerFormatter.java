package org.rekeningsysteem.logic.factuurnummer;

public abstract class AbstractFactuurnummerFormatter implements FactuurnummerFormatter {

  @Override
  public boolean canParse(String factuurnummerString, String jaar) {
    try {
      Factuurnummer f = this.parse(factuurnummerString, jaar);
      return f != null;
    }
    catch (Exception e) {
      return false;
    }
  }
}
