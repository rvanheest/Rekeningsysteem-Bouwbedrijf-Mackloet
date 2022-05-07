package org.rekeningsysteem.data.util;

public interface Factuur<E extends ListItem> extends Document, BedragManager {

	ItemList<E> itemList();

	@Override
	default Totalen getTotalen() {
		return this.itemList().getTotalen();
	}
}
