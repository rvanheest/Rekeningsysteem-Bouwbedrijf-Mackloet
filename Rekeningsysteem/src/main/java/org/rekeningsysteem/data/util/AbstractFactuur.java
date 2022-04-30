package org.rekeningsysteem.data.util;

import java.util.Objects;

import org.rekeningsysteem.data.util.header.FactuurHeader;

public abstract class AbstractFactuur<E extends ListItem> extends AbstractRekening implements BedragManager {

	private final ItemList<E> itemList;

	public AbstractFactuur(FactuurHeader header, ItemList<E> itemList) {
		super(header);
		this.itemList = itemList;
	}

	public ItemList<E> getItemList() {
		return this.itemList;
	}

	@Override
	public Totalen getTotalen() {
		return this.itemList.getTotalen();
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof AbstractFactuur<?> that) {
			return Objects.equals(this.itemList, that.itemList);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.itemList);
	}
}
