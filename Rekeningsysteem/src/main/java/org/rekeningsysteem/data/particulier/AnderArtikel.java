package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class AnderArtikel extends ParticulierArtikel {

	private final String omschrijving;
	private final Geld prijs;
	private final double btwPercentage;

	public AnderArtikel(String omschrijving, Geld prijs, double btwPercentage) {
		this.omschrijving = omschrijving;
		this.prijs = prijs;
		this.btwPercentage = btwPercentage;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public Geld getMateriaal() {
		return new Geld(this.prijs);
	}

	@Override
	public double getMateriaalBtwPercentage() {
		return this.btwPercentage;
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AnderArtikel) {
			AnderArtikel that = (AnderArtikel) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.prijs, that.prijs)
					&& Objects.equals(this.btwPercentage, that.btwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.prijs, this.btwPercentage);
	}

	@Override
	public String toString() {
		return "<AnderArtikel[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.prijs) + ", "
				+ String.valueOf(this.btwPercentage) + "]>";
	}
}
