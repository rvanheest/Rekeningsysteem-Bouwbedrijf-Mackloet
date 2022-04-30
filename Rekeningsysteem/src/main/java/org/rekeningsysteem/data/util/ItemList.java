package org.rekeningsysteem.data.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.exception.DifferentCurrencyException;

public class ItemList<E extends ListItem> implements BedragManager {

	private final List<E> list;
	private final Currency currency;

	public ItemList(Currency currency) {
		this.list = new ArrayList<>();
		this.currency = currency;
	}

	public ItemList(Currency currency, Collection<? extends E> c) {
		this(currency);
		for (E e : c) {
			this.add(e);
		}
	}

	public List<E> getList() {
		return Collections.unmodifiableList(this.list);
	}

	public void add(E item) {
		this.list.add(item);
	}

	public void addAll(Collection<? extends E> items) {
		this.list.addAll(items);
	}

	public void clear() {
		this.list.clear();
	}

	public static <E extends ListItem> ItemList<E> merge(ItemList<? extends E> left, ItemList<? extends E> right) throws DifferentCurrencyException {
		if (left.currency.equals(right.currency)) {
			List<E> list = new ArrayList<>(left.list);
			list.addAll(right.list);
			return new ItemList<>(left.currency, list);
		}
		else
			throw new DifferentCurrencyException(left.currency, right.currency);
	}

	public Stream<E> stream() {
		return this.list.stream();
	}

	public Currency getCurrency() {
		return this.currency;
	}

	@Override
	public Totalen getTotalen() {
		return this.list.parallelStream()
				.reduce(
						Totalen.Empty(),
						(t, li) -> switch (li) {
							case AnderArtikel item -> t.add(item.getMateriaalBtwPercentage(), item.materiaal(), item.getMateriaalBtw());
							case GebruiktEsselinkArtikel item -> t.add(item.getMateriaalBtwPercentage(), item.materiaal(), item.getMateriaalBtw());
							case MutatiesInkoopOrder item -> t.add(item.materiaal());
							case ReparatiesInkoopOrder item -> t.add(item.materiaal());
							case AbstractLoon item -> t.add(item.getLoonBtwPercentage(), item.loon(), item.getLoonBtw());
							default -> t;
						},
						Totalen::plus
				);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ItemList that) {
			return Objects.equals(this.currency, that.currency)
					&& Objects.equals(this.list, that.list);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.list, this.currency);
	}
}
