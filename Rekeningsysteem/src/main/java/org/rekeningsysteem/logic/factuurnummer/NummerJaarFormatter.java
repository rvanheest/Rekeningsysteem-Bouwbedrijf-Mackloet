package org.rekeningsysteem.logic.factuurnummer;

/**
 * Deze formatter produceert factuurnummers als '122020',
 * met '12' als volgnummer en '2020' als jaar
 */
public class NummerJaarFormatter extends AbstractFactuurnummerFormatter {

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
