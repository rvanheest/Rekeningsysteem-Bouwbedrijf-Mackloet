package org.rekeningsysteem.data.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class ParticulierFactuur extends AbstractFactuur<ParticulierArtikel> {

	public ParticulierFactuur(OmschrFactuurHeader header, Currency currency,
			ItemList<ParticulierArtikel> itemList) {
		super(header, currency, itemList);
	}

	@Override
	public OmschrFactuurHeader getFactuurHeader() {
		return (OmschrFactuurHeader) super.getFactuurHeader();
	}

	@Override
	public <T> T accept(RekeningVisitor<T> visitor) throws Exception {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return "<ParticulierFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getCurrency()) + ", "
				+ String.valueOf(this.getItemList()) + "]>";
	}
}
