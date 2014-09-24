package org.rekeningsysteem.data.util;

import java.util.Currency;
import java.util.Objects;

import org.rekeningsysteem.data.util.header.FactuurHeader;

public abstract class AbstractFactuur<E extends ListItem> extends AbstractRekening
		implements BedragManager {

	private Currency currency;
	private final ItemList<E> itemList;
	private final BtwPercentage btwPercentage;

	public AbstractFactuur(FactuurHeader header, Currency currency, ItemList<E> itemList,
			BtwPercentage btwPercentage) {
		super(header);
		this.currency = currency;
		this.itemList = itemList;
		this.btwPercentage = btwPercentage;
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public final void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public ItemList<E> getItemList() {
		return this.itemList;
	}

	public BtwPercentage getBtwPercentage() {
		return this.btwPercentage;
	}

	@Override
	public Totalen getTotalen() {
		Totalen sub = this.itemList.getTotalen();
		Geld loonBtw = sub.getLoon()
				.multiply(this.btwPercentage.getLoonPercentage())
				.divide(100);
		Geld materiaalBtw = sub.getMateriaal()
				.multiply(this.btwPercentage.getMateriaalPercentage())
				.divide(100);
		return sub.withLoonBtw(loonBtw)
				.withMateriaalBtw(materiaalBtw);
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof AbstractFactuur) {
			AbstractFactuur<?> that = (AbstractFactuur<?>) other;
			return Objects.equals(this.currency, that.currency)
					&& Objects.equals(this.itemList, that.itemList)
					&& Objects.equals(this.btwPercentage, that.btwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.currency, this.itemList, this.btwPercentage);
	}
}
