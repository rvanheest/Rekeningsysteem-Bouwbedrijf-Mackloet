package org.rekeningsysteem.data.mutaties;

import org.javamoney.moneta.Money;
import org.rekeningsysteem.data.util.ListItem;

import javax.money.MonetaryAmount;

public record MutatiesInkoopOrder(String omschrijving, String inkoopOrderNummer, MonetaryAmount materiaal) implements ListItem {

	@Override
	public MonetaryAmount loon() {
		return Money.zero(this.materiaal.getCurrency());
	}

	public MonetaryAmount getTotaal() {
		return this.materiaal();
	}
}
