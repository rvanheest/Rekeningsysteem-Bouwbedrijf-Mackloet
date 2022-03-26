package org.rekeningsysteem.ui.textfields.searchbox;

import io.reactivex.rxjava3.core.Observable;

public class SearchBoxController<T> {

	private final AbstractSearchBox<T> ui;
	private final Observable<T> model;

	public SearchBoxController(AbstractSearchBox<T> ui) {
		this.ui = ui;
		this.model = this.ui.getSelectedItem();
	}

	public AbstractSearchBox<T> getUI() {
		return this.ui;
	}

	public Observable<T> getModel() {
		return this.model;
	}
}
