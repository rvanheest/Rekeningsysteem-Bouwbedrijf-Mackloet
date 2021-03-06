package org.rekeningsysteem.data.reparaties;

import java.util.Currency;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.data.util.visitor.RekeningVoidVisitor;

public class ReparatiesFactuur extends AbstractFactuur<ReparatiesInkoopOrder> {

	public ReparatiesFactuur(FactuurHeader header, Currency currency,
			ItemList<ReparatiesInkoopOrder> itemList) {
		super(header, currency, itemList);
	}

	@Override
	public <T> T accept(RekeningVisitor<T> visitor) throws Exception {
		return visitor.visit(this);
	}

	@Override
	public void accept(RekeningVoidVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "<ReparatiesFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getCurrency()) + ", "
				+ String.valueOf(this.getItemList()) + "]>";
	}
}
