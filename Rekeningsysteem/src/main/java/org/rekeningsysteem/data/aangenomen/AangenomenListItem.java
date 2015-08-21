package org.rekeningsysteem.data.aangenomen;

import java.util.Objects;

import org.rekeningsysteem.data.util.BtwListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class AangenomenListItem implements BtwListItem {

	private final String omschrijving;
	private final Geld loon;
	private final double loonBtwPercentage;
	private final Geld materiaal;
	private final double materiaalBtwPercentage;

	public AangenomenListItem(String omschrijving, Geld loon, double loonBtwPercentage,
			Geld materiaal, double materiaalBtwPercentage) {
		this.omschrijving = omschrijving;
		this.loon = loon;
		this.loonBtwPercentage = loonBtwPercentage;
		this.materiaal = materiaal;
		this.materiaalBtwPercentage = materiaalBtwPercentage;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public Geld getLoon() {
		return this.loon;
	}

	@Override
	public double getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}

	@Override
	public Geld getMateriaal() {
		return this.materiaal;
	}

	@Override
	public double getMateriaalBtwPercentage() {
		return this.materiaalBtwPercentage;
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AangenomenListItem) {
			AangenomenListItem that = (AangenomenListItem) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.loon, that.loon)
					&& Objects.equals(this.loonBtwPercentage, that.loonBtwPercentage)
					&& Objects.equals(this.materiaal, that.materiaal)
					&& Objects.equals(this.materiaalBtwPercentage, that.materiaalBtwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.loon, this.loonBtwPercentage,
				this.materiaal, this.materiaalBtwPercentage);
	}

	@Override
	public String toString() {
		return "<AangenomenListItem[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.loon) + ", "
				+ String.valueOf(this.loonBtwPercentage) + ", "
				+ String.valueOf(this.materiaal) + ", "
				+ String.valueOf(this.materiaalBtwPercentage) + "]>";
	}
}
