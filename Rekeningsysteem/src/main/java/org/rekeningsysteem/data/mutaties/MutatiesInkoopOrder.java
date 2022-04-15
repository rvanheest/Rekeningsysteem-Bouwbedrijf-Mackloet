package org.rekeningsysteem.data.mutaties;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public record MutatiesInkoopOrder(String omschrijving, String inkoopOrderNummer, Geld materiaal) implements ListItem {

	@Override
	public Geld loon() {
		return new Geld(0);
	}

	public Geld getTotaal() {
		return this.materiaal();
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
