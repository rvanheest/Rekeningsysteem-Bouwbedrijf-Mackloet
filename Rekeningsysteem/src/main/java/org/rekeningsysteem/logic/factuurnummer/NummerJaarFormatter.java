package org.rekeningsysteem.logic.factuurnummer;

public class NummerJaarFormatter implements FactuurnummerFormatter {

	@Override
	public boolean heeftJaar(String factuurnummer, String jaar) {
		return factuurnummer.endsWith(jaar);
	}

	@Override
	public Factuurnummer parse(String s, String jaar) {
		String nr = s.substring(0, s.lastIndexOf(jaar));
		int nummer = Integer.parseInt(nr);
		return new Factuurnummer(jaar, nummer);
	}

	@Override
	public String format(Factuurnummer nr) {
		return nr.getNummer() + nr.getJaar();
	}
}
