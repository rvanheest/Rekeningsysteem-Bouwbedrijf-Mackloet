package org.rekeningsysteem.data.reparaties;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class ReparatiesFactuur extends AbstractFactuur<ReparatiesBon> {

	public ReparatiesFactuur(FactuurHeader header, String valuta,
			ItemList<ReparatiesBon> itemList, BtwPercentage btwPercentage) {
		super(header, valuta, itemList, btwPercentage);
	}

	@Override
	public void accept(RekeningVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "<ReparatiesFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getValuta()) + ", "
				+ String.valueOf(this.getItemList()) + ", "
				+ String.valueOf(this.getBtwPercentage()) + "]>";
	}
}
