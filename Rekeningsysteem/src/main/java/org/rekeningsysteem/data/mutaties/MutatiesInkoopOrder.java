package org.rekeningsysteem.data.mutaties;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class MutatiesInkoopOrder implements ListItem {

	private final String omschrijving;
	private final String inkoopOrderNummer;
	private final Geld prijs;

	public MutatiesInkoopOrder(String omschrijving, String inkoopOrderNummer, Geld prijs) {
		this.omschrijving = omschrijving;
		this.inkoopOrderNummer = inkoopOrderNummer;
		this.prijs = prijs;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public String getInkoopOrderNummer() {
		return this.inkoopOrderNummer;
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
		if (other instanceof MutatiesInkoopOrder) {
			MutatiesInkoopOrder that = (MutatiesInkoopOrder) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.inkoopOrderNummer, that.inkoopOrderNummer)
					&& Objects.equals(this.prijs, that.prijs);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.inkoopOrderNummer, this.prijs);
	}

	@Override
	public String toString() {
		return "<MutatiesInkoopOrder[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.inkoopOrderNummer) + ", "
				+ String.valueOf(this.prijs) + "]>";
	}
}
