package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class AnderArtikel extends ParticulierArtikel {

	private final Geld prijs;
	private final BtwPercentage btwPercentage;

	public AnderArtikel(String omschrijving, Geld prijs, BtwPercentage btwPercentage) {
		super(omschrijving);
		this.prijs = prijs;
		this.btwPercentage = btwPercentage;
	}

	@Override
	public Geld getMateriaal() {
		return this.prijs;
	}

	@Override
	public BtwPercentage getMateriaalBtwPercentage() {
		return this.btwPercentage;
	}

	@Override
	public final Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public final BtwPercentage getLoonBtwPercentage() {
		return new BtwPercentage(0, this.btwPercentage.isVerlegd());
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
		if (other instanceof AnderArtikel) {
			AnderArtikel that = (AnderArtikel) other;
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
		return "<AnderArtikel[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.prijs) + ", "
				+ String.valueOf(this.btwPercentage) + "]>";
	}
}
