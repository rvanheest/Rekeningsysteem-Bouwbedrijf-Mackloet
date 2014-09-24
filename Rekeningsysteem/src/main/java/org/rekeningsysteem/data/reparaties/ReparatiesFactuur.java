package org.rekeningsysteem.data.reparaties;

import java.util.Currency;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class ReparatiesFactuur extends AbstractFactuur<ReparatiesBon> {

	public ReparatiesFactuur(FactuurHeader header, Currency currency,
			ItemList<ReparatiesBon> itemList, BtwPercentage btwPercentage) {
		super(header, currency, itemList, btwPercentage);
	}

	@Override
	public void accept(RekeningVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "<ReparatiesFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getCurrency()) + ", "
				+ String.valueOf(this.getItemList()) + ", "
				+ String.valueOf(this.getBtwPercentage()) + "]>";
	}
}
