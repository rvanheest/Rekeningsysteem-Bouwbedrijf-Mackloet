package org.rekeningsysteem.data.mutaties;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class MutatiesBon implements ListItem {

	private final String omschrijving;
	private final String bonnummer;
	private final Geld prijs;

	public MutatiesBon(String omschrijving, String bonnummer, Geld prijs) {
		this.omschrijving = omschrijving;
		this.bonnummer = bonnummer;
		this.prijs = prijs;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public String getBonnummer() {
		return this.bonnummer;
	}

	@Override
	public final Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public Geld getMateriaal() {
		return this.prijs;
	}

	public Geld getTotaal() {
		return this.getMateriaal();
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof MutatiesBon) {
			MutatiesBon that = (MutatiesBon) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.bonnummer, that.bonnummer)
					&& Objects.equals(this.prijs, that.prijs);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.bonnummer, this.prijs);
	}

	@Override
	public String toString() {
		return "<MutatiesBon[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.bonnummer) + ", "
				+ String.valueOf(this.prijs) + "]>";
	}
}
