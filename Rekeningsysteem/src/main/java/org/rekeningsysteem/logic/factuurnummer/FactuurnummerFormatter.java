package org.rekeningsysteem.logic.factuurnummer;

public interface FactuurnummerFormatter {
	boolean heeftJaar(String factuurnummer, String jaar);

	Factuurnummer parse(String s, String jaar);

	String format(Factuurnummer nr);
}
