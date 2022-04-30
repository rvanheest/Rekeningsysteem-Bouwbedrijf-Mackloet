package org.rekeningsysteem.data.util;

import java.util.ArrayList;
import java.util.Collection;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;

public class ItemList<E extends ListItem> extends ArrayList<E> implements BedragManager {

	private static final long serialVersionUID = -8022736753592974322L;

	public ItemList() {
		super();
	}

	public ItemList(Collection<? extends E> c) {
		super(c);
	}

	@Override
	public Totalen getTotalen() {
		return this.parallelStream()
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
}
