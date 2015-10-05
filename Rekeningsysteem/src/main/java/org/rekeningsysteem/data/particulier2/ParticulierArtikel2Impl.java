package org.rekeningsysteem.data.particulier2;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

// TODO AnderArtikel
public class ParticulierArtikel2Impl implements ParticulierArtikel2 {

	private final String omschrijving;
	private final Geld prijs;
	private final double btwPercentage;

	public ParticulierArtikel2Impl(String omschrijving, Geld prijs, double btwPercentage) {
		this.omschrijving = omschrijving;
		this.prijs = prijs;
		this.btwPercentage = btwPercentage;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public Geld getMateriaal() {
		return this.prijs;
	}

	@Override
	public double getMateriaalBtwPercentage() {
		return this.btwPercentage;
	}

	@Override
	public final Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public final double getLoonBtwPercentage() {
		return 0;
	}

	@Override
	public final Geld getLoonBtw() {
		return new Geld(0);
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ParticulierArtikel2Impl) {
			ParticulierArtikel2Impl that = (ParticulierArtikel2Impl) other;
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
		return "<ParticulierArtikelImpl[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.prijs) + ", "
				+ String.valueOf(this.btwPercentage) + "]>";
	}
}
