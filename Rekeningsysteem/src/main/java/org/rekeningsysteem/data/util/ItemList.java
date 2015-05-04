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
		return t.withLoon(t.getLoon().add(li.getLoon()))
				.withMateriaal(t.getMateriaal().add(li.getMateriaal()));
	}
}
