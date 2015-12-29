package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

// TODO AnderArtikel
public class ParticulierArtikel2Impl extends ParticulierArtikel2 {

	private final Geld prijs;
	private final double btwPercentage;

	public ParticulierArtikel2Impl(String omschrijving, Geld prijs, double btwPercentage) {
		super(omschrijving);
		this.prijs = prijs;
		this.btwPercentage = btwPercentage;
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
			return super.equals(that)
					&& Objects.equals(this.prijs, that.prijs)
					&& Objects.equals(this.btwPercentage, that.btwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.prijs, this.btwPercentage);
	}

	@Override
	public String toString() {
		return "<ParticulierArtikelImpl[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.prijs) + ", "
				+ String.valueOf(this.btwPercentage) + "]>";
	}
}
