package org.rekeningsysteem.data.reparaties;

import org.rekeningsysteem.data.util.ListItem;

import javax.money.MonetaryAmount;

public record ReparatiesInkoopOrder(String omschrijving, String inkoopOrderNummer, MonetaryAmount loon, MonetaryAmount materiaal) implements ListItem {

	public MonetaryAmount getTotaal() {
		return this.loon.add(this.materiaal);
	}
}
