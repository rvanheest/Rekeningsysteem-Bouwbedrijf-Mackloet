package org.rekeningsysteem.data.reparaties;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class ReparatiesBon implements ListItem {

	private final String omschrijving;
	private final String bonnummer;
	private final Geld loon;
	private final Geld materiaal;
	private final Geld totaal;

	public ReparatiesBon(String omschrijving, String bonnummer, Geld loon, Geld materiaal) {
		this.omschrijving = omschrijving;
		this.bonnummer = bonnummer;
		this.loon = loon;
		this.materiaal = materiaal;
		this.totaal = this.loon.add(this.materiaal);
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public String getBonnummer() {
		return this.bonnummer;
	}

	@Override
	public Geld getLoon() {
		return new Geld(this.loon);
	}

	@Override
	public Geld getMateriaal() {
		return new Geld(this.materiaal);
	}

	@Override
	public Geld getTotaal() {
		return new Geld(this.totaal);
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ReparatiesBon) {
			ReparatiesBon that = (ReparatiesBon) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.bonnummer, that.bonnummer)
					&& Objects.equals(this.loon, that.loon)
					&& Objects.equals(this.materiaal, that.materiaal);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.bonnummer, this.loon, this.materiaal);
	}

	@Override
	public String toString() {
		return "<ReparatiesBon[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.bonnummer) + ", "
				+ String.valueOf(this.loon) + ", "
				+ String.valueOf(this.materiaal) + "]>";
	}
}
