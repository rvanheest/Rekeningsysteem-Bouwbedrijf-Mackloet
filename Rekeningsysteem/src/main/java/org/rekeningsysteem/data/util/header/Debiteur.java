package org.rekeningsysteem.data.util.header;

import java.util.Objects;
import java.util.Optional;

public final class Debiteur {

	private final String naam;
	private final String straat;
	private final String nummer;
	private final String postcode;
	private final String plaats;
	private final Optional<String> btwNummer;

	public Debiteur(String naam, String straat, String nummer, String postcode, String plaats) {
		this(naam, straat, nummer, postcode, plaats, Optional.empty());
	}

	public Debiteur(String naam, String straat, String nummer, String postcode, String plaats,
			String btwNummer) {
		this(naam, straat, nummer, postcode, plaats, Optional.of(btwNummer));
	}

	private Debiteur(String naam, String straat, String nummer, String postcode, String plaats,
			Optional<String> btwNummer) {
		this.naam = naam;
		this.straat = straat;
		this.nummer = nummer;
		this.postcode = postcode;
		this.plaats = plaats;
		this.btwNummer = btwNummer;
	}

	public String getNaam() {
		return this.naam;
	}

	public String getStraat() {
		return this.straat;
	}

	public String getNummer() {
		return this.nummer;
	}

	public String getPostcode() {
		return this.postcode;
	}

	public String getPlaats() {
		return this.plaats;
	}

	public Optional<String> getBtwNummer() {
		return this.btwNummer;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Debiteur) {
			Debiteur that = (Debiteur) other;
			return Objects.equals(this.naam, that.naam)
					&& Objects.equals(this.straat, that.straat)
					&& Objects.equals(this.nummer, that.nummer)
					&& Objects.equals(this.postcode, that.postcode)
					&& Objects.equals(this.plaats, that.plaats)
					&& Objects.equals(this.btwNummer, that.btwNummer);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.naam, this.straat, this.nummer, this.postcode, this.plaats,
				this.btwNummer);
	}

	@Override
	public String toString() {
		return "<Debiteur[" + String.valueOf(this.naam) + ", "
				+ String.valueOf(this.straat) + ", "
				+ String.valueOf(this.nummer) + ", "
				+ String.valueOf(this.postcode) + ", "
				+ String.valueOf(this.plaats) + ", "
				+ String.valueOf(this.btwNummer) + "]>";
	}
}
