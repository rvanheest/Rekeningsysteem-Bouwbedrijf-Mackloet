package org.rekeningsysteem.data.particulier2.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class InstantLoon2 extends AbstractLoon2 {

	private final Geld loon;
	private final double loonBtwPercentage;

	public InstantLoon2(String omschrijving, Geld loon, double loonBtwPercentage) {
		super(omschrijving);
		this.loon = loon;
		this.loonBtwPercentage = loonBtwPercentage;
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
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof InstantLoon2) {
			InstantLoon2 that = (InstantLoon2) other;
			return Objects.equals(this.loon, that.loon)
					&& Objects.equals(this.loonBtwPercentage, that.loonBtwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.loon, this.loonBtwPercentage);
	}

	@Override
	public String toString() {
		return "<InstantLoon2[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.loon) + ", "
				+ String.valueOf(this.loonBtwPercentage) + "]>";
	}
}
