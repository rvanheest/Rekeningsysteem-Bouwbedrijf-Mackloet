package org.rekeningsysteem.data.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Objects;

import org.rekeningsysteem.exception.GeldParseException;

public final class Geld {

	private double bedrag;

	public Geld(double bedrag) {
		this.setBedrag(bedrag);
	}

	public Geld(String bedrag) throws GeldParseException {
		this.setBedrag(bedrag);
	}

	public Geld(Geld geld) {
		this(geld.getBedrag());
	}

	public double getBedrag() {
		return this.bedrag;
	}

	private void setBedrag(double bedrag) {
		this.bedrag = Double.valueOf(Math.round(bedrag * 100)) / 100;
	}

	private void setBedrag(String bedrag) throws GeldParseException {
		// (-?) = misschien een '-'symbool (negatie)
		// (\\d?\\d?\\d?\\.?)* = cijfers voor de komma misschien gegroepeerd in drietallen,
		// eventueel gevolgd door een punt
		// (,\\d+)? = misschien een komma, gevolgd door een onbepaalde hoeveelheid decimalen.
		if (bedrag.matches("^(-?)(\\d?\\d?\\d?\\.?)*(,\\d+)?$")) {
			String modified = bedrag.replaceAll("\\.", "").replace(',', '.');
			this.setBedrag(Double.parseDouble(modified));
		}
		else if (bedrag.matches("^(-?)(\\d?\\d?\\d?\\,?)*(.\\d+)?$")) {
			String modified = bedrag.replaceAll("\\,", "");
			this.setBedrag(Double.parseDouble(modified));
		}
		else {
			throw new GeldParseException("Kan de String '" + bedrag + "' niet parsen naar een "
					+ "geldig Geld-bedrag.");
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
		return new Geld(this.bedrag / getal);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Geld) {
			Geld that = (Geld) other;
			return Objects.equals(this.bedrag, that.bedrag);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.bedrag);
	}

	@Override
	public String toString() {
		return "<Geld[" + this.formattedString() + "]>";
	}
}
