package org.rekeningsysteem.data.aangenomen;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class AangenomenFactuur extends AbstractFactuur<AangenomenListItem> {

	public AangenomenFactuur(OmschrFactuurHeader header, String valuta,
			ItemList<AangenomenListItem> itemList) {
		super(header, valuta, itemList);
	}

	@Override
	public OmschrFactuurHeader getFactuurHeader() {
		return (OmschrFactuurHeader) super.getFactuurHeader();
	}

	@Override
	public void accept(RekeningVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "<AangenomenFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getValuta()) + ", "
				+ String.valueOf(this.getItemList()) + "]>";
	}
}
