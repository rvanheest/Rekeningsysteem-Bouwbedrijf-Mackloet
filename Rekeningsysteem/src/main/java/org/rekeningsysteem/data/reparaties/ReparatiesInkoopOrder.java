package org.rekeningsysteem.data.reparaties;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;

public record ReparatiesInkoopOrder(String omschrijving, String inkoopOrderNummer, Geld loon, Geld materiaal) implements ListItem {

	public Geld getTotaal() {
		return this.loon.add(this.materiaal);
	}
}
