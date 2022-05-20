package org.rekeningsysteem.data.util;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.loon.Loon;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;

import javax.money.CurrencyUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ItemList<E extends ListItem> implements BedragManager {

	private final List<E> list;
	private final CurrencyUnit currency;

	public ItemList(CurrencyUnit currency) {
		this.list = new ArrayList<>();
		this.currency = currency;
	}

	public ItemList(CurrencyUnit currency, Collection<? extends E> c) {
		this(currency);
		this.list.addAll(c);
	}

	public Stream<E> stream() {
		return this.list.stream();
	}

	public CurrencyUnit getCurrency() {
		return this.currency;
	}

	@Override
	public Totalen getTotalen() {
		return this.list.parallelStream()
				.reduce(
						new Totalen(this.currency),
						(t, li) -> switch (li) {
							case AnderArtikel item -> t.add(item.materiaalBtwPercentage(), item.materiaal(), item.materiaalBtw());
							case GebruiktEsselinkArtikel item -> t.add(item.materiaalBtwPercentage(), item.materiaal(), item.materiaalBtw());
							case MutatiesInkoopOrder item -> t.add(item.materiaal());
							case ReparatiesInkoopOrder item -> t.add(item.materiaal());
							case Loon item -> t.add(item.loonBtwPercentage(), item.loon(), item.loonBtw());
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
