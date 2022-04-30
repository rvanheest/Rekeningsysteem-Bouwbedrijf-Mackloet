package org.rekeningsysteem.data.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class ItemList<E extends ListItem> extends ArrayList<E> implements BedragManager {

	private static final long serialVersionUID = -8022736753592974322L;

	private final ListItemVisitor<Function<Totalen, Totalen>> visitor;

	public ItemList() {
		this(new TotalenListItemVisitor());
	}

	public ItemList(ListItemVisitor<Function<Totalen, Totalen>> visitor) {
		super();
		this.visitor = visitor;
	}

	public ItemList(Collection<? extends E> c) {
		this(new TotalenListItemVisitor(), c);
	}

	public ItemList(ListItemVisitor<Function<Totalen, Totalen>> visitor, Collection<? extends E> c) {
		super(c);
		this.visitor = visitor;
	}

	@Override
	public Totalen getTotalen() {
		return this.parallelStream().reduce(Totalen.Empty(), (t, li) -> li.accept(this.visitor).apply(t), Totalen::plus);
	}
}
