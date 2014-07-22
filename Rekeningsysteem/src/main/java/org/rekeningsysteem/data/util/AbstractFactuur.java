package org.rekeningsysteem.data.util;

import java.util.Objects;

import org.rekeningsysteem.data.util.header.FactuurHeader;

public abstract class AbstractFactuur<E extends ListItem> extends AbstractRekening {

	private String valuta;
	private final ItemList<E> itemList;

	public AbstractFactuur(FactuurHeader header, String valuta, ItemList<E> itemList) {
		super(header);
		this.valuta = valuta;
		this.itemList = itemList;
	}

	public String getValuta() {
		return this.valuta;
	}

	public final void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public ItemList<E> getItemList() {
		return this.itemList;
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof AbstractFactuur) {
			AbstractFactuur<?> that = (AbstractFactuur<?>) other;
			return Objects.equals(this.valuta, that.valuta)
					&& Objects.equals(this.itemList, that.itemList);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.valuta, this.itemList);
	}
}
