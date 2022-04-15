package org.rekeningsysteem.data.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.rekeningsysteem.exception.GeldParseException;

public record Geld(double bedrag) {

	public Geld(double bedrag) {
		this.bedrag = (double) Math.round(bedrag * 100) / 100;
	}

	public Geld(String bedrag) throws GeldParseException {
		this(parse(bedrag));
	}

	private static double parse(String bedrag) throws GeldParseException {
		// (-?) = misschien een '-'symbool (negatie)
		// (\\d?\\d?\\d?\\.?)* = cijfers voor de komma misschien gegroepeerd in drietallen,
		// eventueel gevolgd door een punt
		// (,\\d+)? = misschien een komma, gevolgd door een onbepaalde hoeveelheid decimalen.
		if (bedrag.matches("^(-?)(\\d?\\d?\\d?\\.?)*(,\\d+)?$")) {
			String modified = bedrag.replaceAll("\\.", "").replace(',', '.');
			return (double) Math.round(Double.parseDouble(modified) * 100) / 100;
		}
		else if (bedrag.matches("^(-?)(\\d?\\d?\\d?,?)*(.\\d+)?$")) {
			String modified = bedrag.replaceAll(",", "");
			return (double) Math.round(Double.parseDouble(modified) * 100) / 100;
		}
		else {
			throw new GeldParseException("Kan de String '" + bedrag + "' niet parsen naar een geldig Geld-bedrag.");
		}
	}

	public String formattedString() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator('.');

		DecimalFormat format = new DecimalFormat("#,###.00", symbols);
		format.setGroupingSize(3);

		String formatted = format.format(this.bedrag);

		if (Double.compare(this.bedrag, 0.0) >= 0) {
			if (Double.compare(this.bedrag, 1.0) == -1) {
				return "0" + formatted;
			}
		}
		else {
			if (Double.compare(this.bedrag, -1.0) == 1) {
				return new StringBuffer(formatted).insert(1, "0").toString();
			}
		}
		return format.format(this.bedrag);
	}

	public boolean isZero() {
		return Double.compare(this.bedrag, 0.0) == 0;
	}

	public Geld add(Geld geld) {
		return new Geld(this.bedrag + geld.bedrag);
	}

	public Geld subtract(Geld geld) {
		return new Geld(this.bedrag - geld.bedrag);
	}

	public Geld multiply(double getal) {
		return new Geld(this.bedrag * getal);
	}

	public Geld divide(double getal) {
		assert Double.compare(getal, 0.0) != 0;
		return new Geld(this.bedrag / getal);
	}
}
