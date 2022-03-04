package org.rekeningsysteem.logic.factuurnummer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Deze formatter produceert factuurnummers als '20201012',
 * met '2020' als jaar, '1' als bedrijfskenmerk en '012' als volgnummer 
 */
public class JaarKenmerkNummerFormatter implements FactuurnummerFormatter {
	
	private final String kenmerk;

	public JaarKenmerkNummerFormatter(String kenmerk) {
		this.kenmerk = kenmerk;
	}

	@Override
	public boolean heeftJaar(String factuurnummer, String jaar) {
		return factuurnummer.startsWith(jaar);
	}

	@Override
	public Factuurnummer parse(String factuurnummerString, String jaar) {
		Pattern pattern = Pattern.compile("^([0-9]{4})" + this.kenmerk + "([0-9]*)$");
		Matcher matcher = pattern.matcher(factuurnummerString);
		if (matcher.matches()) {
			String jr = matcher.group(1);
			String nr = matcher.group(2);
			return new Factuurnummer(jr, Integer.parseInt(nr));
		}

		throw new NumberFormatException(String.format("Factuurnummer %s is ongeldig", factuurnummerString));
	}

	@Override
	public String format(Factuurnummer nr) {
		return nr.getJaar() + this.kenmerk + String.format("%03d", nr.getNummer());
	}
}
