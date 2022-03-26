package org.rekeningsysteem.ui.list;

import io.reactivex.rxjava3.core.Maybe;

import java.util.Optional;

public abstract class AbstractListItemController<M> {

	private final ItemPane ui;
	private final Maybe<Optional<M>> model;

	public AbstractListItemController(ItemPane ui, Maybe<Optional<M>> model) {
		this.ui = ui;
		this.model = model;
	}

	public ItemPane getUI() {
		return this.ui;
	}

	public Maybe<Optional<M>> getModel() {
		return this.model;
	}
}
