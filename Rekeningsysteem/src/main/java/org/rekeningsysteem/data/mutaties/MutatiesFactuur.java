package org.rekeningsysteem.data.mutaties;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class MutatiesFactuur extends AbstractFactuur<MutatiesBon> {

	public MutatiesFactuur(FactuurHeader header, String valuta,
			ItemList<MutatiesBon> itemList, BtwPercentage btwPercentage) {
		super(header, valuta, itemList, btwPercentage);
	}

	@Override
	public void accept(RekeningVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "<MutatiesFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getValuta()) + ", "
				+ String.valueOf(this.getItemList()) + ", "
				+ String.valueOf(this.getBtwPercentage()) + "]>";
	}
}
