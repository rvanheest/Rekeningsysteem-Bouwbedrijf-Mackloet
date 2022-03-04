package org.rekeningsysteem.logic.factuurnummer;

public interface FactuurnummerFormatter {
	boolean heeftJaar(String factuurnummer, String jaar);

	Factuurnummer parse(String factuurnummerString, String jaar);

	String format(Factuurnummer nr);
}
