package org.rekeningsysteem.logic.factuurnummer;

public class NummerJaarFormatter implements FactuurnummerFormatter {

	@Override
	public boolean heeftJaar(String factuurnummer, String jaar) {
		return factuurnummer.endsWith(jaar);
	}

	@Override
	public Factuurnummer parse(String factuurnummerString, String jaar) {
		String nr = factuurnummerString.substring(0, factuurnummerString.lastIndexOf(jaar));
		int nummer = Integer.parseInt(nr);
		return new Factuurnummer(jaar, nummer);
	}

	@Override
	public String format(Factuurnummer nr) {
		return nr.getNummer() + nr.getJaar();
	}
}
