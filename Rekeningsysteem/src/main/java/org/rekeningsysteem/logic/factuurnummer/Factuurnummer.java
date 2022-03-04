package org.rekeningsysteem.logic.factuurnummer;

import java.util.Objects;

public class Factuurnummer {

	private final String jaar;
	private final int nummer;

	public Factuurnummer(String jaar) {
		this(jaar, 1);
	}

	public Factuurnummer(String jaar, int nummer) {
		this.jaar = jaar;
		this.nummer = nummer;
	}

	public String getJaar() {
		return this.jaar;
	}

	public int getNummer() {
		return this.nummer;
	}

	public Factuurnummer next() {
		return new Factuurnummer(this.jaar, this.nummer + 1);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Factuurnummer) {
			Factuurnummer that = (Factuurnummer) other;
			return Objects.equals(this.jaar, that.jaar) && Objects.equals(this.nummer, that.nummer);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.jaar, this.nummer);
	}

	@Override
	public String toString() {
		return "<Factuurnummer[" + String.valueOf(this.jaar) + ", " + String.valueOf(this.nummer) + "]>";
	}
}
