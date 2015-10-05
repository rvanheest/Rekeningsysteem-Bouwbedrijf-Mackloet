package org.rekeningsysteem.data.util.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

@Deprecated
public final class InstantLoon extends AbstractLoon {

	private final Geld loon;
	private final double loonBtwPercentage;

	public InstantLoon(String omschrijving, Geld loon, double loonBtwPercentage) {
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
		if (super.equals(other) && other instanceof InstantLoon) {
			InstantLoon that = (InstantLoon) other;
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
		return "<InstantLoon[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.loon) + ", "
				+ String.valueOf(this.loonBtwPercentage) + "]>";
	}
}
