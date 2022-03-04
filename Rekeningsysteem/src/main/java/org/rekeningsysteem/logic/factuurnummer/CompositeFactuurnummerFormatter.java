package org.rekeningsysteem.logic.factuurnummer;

import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;

public class CompositeFactuurnummerFormatter extends AbstractFactuurnummerFormatter {

  private final FactuurnummerFormatter oldFormatter;
  private final FactuurnummerFormatter newFormatter;

  public CompositeFactuurnummerFormatter(FactuurnummerFormatter oldFormatter, FactuurnummerFormatter newFormatter) {
    this.oldFormatter = oldFormatter;
    this.newFormatter = newFormatter;
  }

  public static FactuurnummerFormatter Create(String kenmerk) {
    return new CompositeFactuurnummerFormatter(
        new NummerJaarFormatter(),
        new JaarKenmerkNummerFormatter(kenmerk)
    );
  }

  public static FactuurnummerFormatter Create(PropertiesWorker worker, PropertyKey key) {
    return worker.getProperty(key)
        .map(CompositeFactuurnummerFormatter::Create)
        .orElseGet(NummerJaarFormatter::new);
  }

  @Override
  public boolean heeftJaar(String factuurnummer, String jaar) {
    if (this.newFormatter.canParse(factuurnummer, jaar))
      return this.newFormatter.heeftJaar(factuurnummer, jaar);

    if (this.oldFormatter.canParse(factuurnummer, jaar))
      return this.oldFormatter.heeftJaar(factuurnummer, jaar);

    return false;
  }

  @Override
  public Factuurnummer parse(String factuurnummerString, String jaar) {
    if (this.newFormatter.canParse(factuurnummerString, jaar))
      return this.newFormatter.parse(factuurnummerString, jaar);

    if (this.oldFormatter.canParse(factuurnummerString, jaar))
      return this.oldFormatter.parse(factuurnummerString, jaar);

    return null;
  }

  @Override
  public String format(Factuurnummer nr) {
    return this.newFormatter.format(nr);
  }
}
