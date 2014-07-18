package org.rekeningsysteem.logic.factuurnummer.guice;

import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;
import org.rekeningsysteem.properties.PropertyKey;

public interface FactuurnummerManagerFactory {

	FactuurnummerManager create(PropertyKey key);
}
