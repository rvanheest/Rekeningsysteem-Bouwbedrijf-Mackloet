package org.rekeningsysteem.data.util.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class InstantLoon extends AbstractLoon {

	private final Geld loon;

	public InstantLoon(String omschrijving, Geld loon) {
		super(omschrijving);
		this.loon = loon;
	}

	@Override
	public Geld getLoon() {
		return new Geld(this.loon);
	}
	
	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof InstantLoon) {
			InstantLoon that = (InstantLoon) other;
			return Objects.equals(this.loon, that.loon);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.loon);
	}

	@Override
	public String toString() {
		return "<InstantLoon[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.loon) + "]>";
	}
}
