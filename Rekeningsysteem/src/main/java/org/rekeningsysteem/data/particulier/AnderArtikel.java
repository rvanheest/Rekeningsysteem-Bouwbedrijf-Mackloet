package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;

public final class AnderArtikel extends ParticulierArtikel {

	private final String omschrijving;
	private final Geld prijs;

	public AnderArtikel(String omschrijving, Geld prijs) {
		this.omschrijving = omschrijving;
		this.prijs = prijs;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public Geld getMateriaal() {
		return new Geld(this.prijs);
	}

	@Override
	public Geld getTotaal() {
		return this.getMateriaal();
	}

	@Override
	public String[] toArray() {
		return new String[] {
				"",
				this.omschrijving,
				"",
				"",
				this.prijs.formattedString(),
				""
		};
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AnderArtikel) {
			AnderArtikel that = (AnderArtikel) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.prijs, that.prijs);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.prijs);
	}

	@Override
	public String toString() {
		return "<AnderArtikel[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.prijs) + "]>";
	}
}
