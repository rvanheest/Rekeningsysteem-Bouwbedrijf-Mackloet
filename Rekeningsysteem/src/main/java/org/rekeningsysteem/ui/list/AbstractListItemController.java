package org.rekeningsysteem.ui.list;

import java.util.Optional;

import org.rekeningsysteem.data.util.ListItem;

import rx.Observable;

public abstract class AbstractListItemController<M extends ListItem> {

	private final ItemPane ui;
	private final Observable<Optional<M>> model;

	public AbstractListItemController(ItemPane ui, Observable<Optional<M>> model) {
		this.ui = ui;
		this.model = model;
	}

	public ItemPane getUI() {
		return this.ui;
	}

	public Observable<Optional<M>> getModel() {
		return this.model;
	}
}
