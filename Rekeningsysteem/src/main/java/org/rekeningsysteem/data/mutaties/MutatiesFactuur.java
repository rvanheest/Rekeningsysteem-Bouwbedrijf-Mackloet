package org.rekeningsysteem.data.mutaties;

import java.util.Currency;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.data.util.visitor.RekeningVoidVisitor;

public class MutatiesFactuur extends AbstractFactuur<MutatiesBon> {

	public MutatiesFactuur(FactuurHeader header, Currency currency,
			ItemList<MutatiesBon> itemList) {
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
		return "<MutatiesFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getCurrency()) + ", "
				+ String.valueOf(this.getItemList()) + "]>";
	}
}
