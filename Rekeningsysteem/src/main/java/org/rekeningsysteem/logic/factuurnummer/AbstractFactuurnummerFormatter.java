package org.rekeningsysteem.logic.factuurnummer;

public abstract class AbstractFactuurnummerFormatter implements FactuurnummerFormatter {

  @Override
  public boolean canParse(String factuurnummerString, String jaar) {
    try {
		return this.parse(factuurnummerString, jaar) != null;
    }
    catch (Exception e) {
      return false;
    }
  }
}
