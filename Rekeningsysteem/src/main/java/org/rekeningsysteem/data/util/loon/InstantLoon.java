package org.rekeningsysteem.data.util.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;

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
	public boolean equals(Object other) {
		if (other instanceof InstantLoon) {
			InstantLoon that = (InstantLoon) other;
			return super.equals(that)
					&& Objects.equals(this.loon, that.loon);
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
