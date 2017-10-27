package org.rekeningsysteem.data.reparaties;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class ReparatiesInkoopOrder implements ListItem {

	private final String omschrijving;
	private final String inkoopOrderNummer;
	private final Geld loon;
	private final Geld materiaal;
	private final Geld totaal;

	public ReparatiesInkoopOrder(String omschrijving, String inkoopOrderNummer, Geld loon, Geld materiaal) {
		this.omschrijving = omschrijving;
		this.inkoopOrderNummer = inkoopOrderNummer;
		this.loon = loon;
		this.materiaal = materiaal;
		this.totaal = this.loon.add(this.materiaal);
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public String getInkoopOrderNummer() {
		return this.inkoopOrderNummer;
	}

	@Override
	public Geld getLoon() {
		return this.loon;
	}

	@Override
	public Geld getMateriaal() {
		return this.materiaal;
	}

	public Geld getTotaal() {
		return this.totaal;
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ReparatiesInkoopOrder) {
			ReparatiesInkoopOrder that = (ReparatiesInkoopOrder) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.inkoopOrderNummer, that.inkoopOrderNummer)
					&& Objects.equals(this.loon, that.loon)
					&& Objects.equals(this.materiaal, that.materiaal);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.inkoopOrderNummer, this.loon, this.materiaal);
	}

	@Override
	public String toString() {
		return "<ReparatiesInkoopOrder[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.inkoopOrderNummer) + ", "
				+ String.valueOf(this.loon) + ", "
				+ String.valueOf(this.materiaal) + "]>";
	}
}
