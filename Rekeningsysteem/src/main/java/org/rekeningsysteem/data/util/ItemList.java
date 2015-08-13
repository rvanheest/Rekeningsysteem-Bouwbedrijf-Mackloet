package org.rekeningsysteem.data.util;

import java.util.ArrayList;
import java.util.Collection;

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
		return this.parallelStream().reduce(new Totalen(), ItemList::makeTotalen, Totalen::plus);
	}

	protected static Totalen makeTotalen(Totalen t, ListItem li) {
		if (li instanceof BtwListItem) {
			return makeBtwTotalen(t, (BtwListItem) li);
		}
		return t.addLoon(li.getLoon())
				.addMateriaal(li.getMateriaal());
	}

	protected static Totalen makeBtwTotalen(Totalen t, BtwListItem li) {
		return t.addLoon(li.getLoon())
				.addBtw(li.getLoonBtwPercentage(), li.getLoonBtw())
				.addMateriaal(li.getMateriaal())
				.addBtw(li.getMateriaalBtwPercentage(), li.getMateriaalBtw());
	}
}
