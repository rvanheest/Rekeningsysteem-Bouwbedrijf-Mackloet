package org.rekeningsysteem.data.mutaties;

import java.util.Currency;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public class MutatiesFactuur extends AbstractFactuur<MutatiesInkoopOrder> {

	public MutatiesFactuur(FactuurHeader header, Currency currency, ItemList<MutatiesInkoopOrder> itemList) {
		super(header, currency, itemList);
	}

	@Override
	public String toString() {
		return "<MutatiesFactuur["
			+ String.valueOf(this.getFactuurHeader()) + ", "
			+ String.valueOf(this.getCurrency()) + ", "
			+ String.valueOf(this.getItemList()) + "]>";
	}
}
