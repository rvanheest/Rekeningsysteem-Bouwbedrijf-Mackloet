package org.rekeningsysteem.data.reparaties;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public class ReparatiesFactuur extends AbstractFactuur<ReparatiesInkoopOrder> {

	public ReparatiesFactuur(FactuurHeader header, ItemList<ReparatiesInkoopOrder> itemList) {
		super(header, itemList);
	}

	@Override
	public String toString() {
		return "<ReparatiesFactuur["
			+ String.valueOf(this.getFactuurHeader()) + ", "
			+ String.valueOf(this.getItemList()) + "]>";
	}
}
