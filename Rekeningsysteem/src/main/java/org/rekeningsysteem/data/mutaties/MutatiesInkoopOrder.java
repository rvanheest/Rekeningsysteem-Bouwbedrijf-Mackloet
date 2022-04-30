package org.rekeningsysteem.data.mutaties;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;

public record MutatiesInkoopOrder(String omschrijving, String inkoopOrderNummer, Geld materiaal) implements ListItem {

	@Override
	public Geld loon() {
		return new Geld(0);
	}

	public Geld getTotaal() {
		return this.materiaal();
	}
}
