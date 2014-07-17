package org.rekeningsysteem.data.util;

import java.util.List;
import java.util.Objects;

import org.rekeningsysteem.data.util.header.FactuurHeader;

public abstract class AbstractFactuur<E extends ListItem> extends AbstractRekening {

	private String valuta;
	private final List<E> itemList;
	private BtwPercentage btwPercentage;

	public AbstractFactuur(FactuurHeader header, String valuta, List<E> itemList,
			BtwPercentage btwPercentage) {
		super(header);
		this.valuta = valuta;
		this.itemList = itemList;
		this.btwPercentage = btwPercentage;
	}

	public String getValuta() {
		return this.valuta;
	}

	public final void setValuta(String valuta) {
		this.valuta = valuta;
	}

	public List<E> getItemList() {
		return this.itemList;
	}

	public BtwPercentage getBtwPercentage() {
		return this.btwPercentage;
	}

	public void setBtwPercentage(BtwPercentage btwPercentage) {
		this.btwPercentage = btwPercentage;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractFactuur) {
			AbstractFactuur<?> that = (AbstractFactuur<?>) other;
			return super.equals(that)
					&& Objects.equals(this.valuta, that.valuta)
					&& Objects.equals(this.itemList, that.itemList)
					&& Objects.equals(this.btwPercentage, that.btwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.valuta, this.itemList, this.btwPercentage);
	}
}
