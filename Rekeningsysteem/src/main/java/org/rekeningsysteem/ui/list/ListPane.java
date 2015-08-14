package org.rekeningsysteem.ui.list;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;
import org.rekeningsysteem.ui.WorkingPane;

public class ListPane extends WorkingPane {

	public ListPane(Page listPane) {
		super(listPane);

		Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.map(d -> Math.min(d, 700))
				.doOnNext(listPane::setPrefHeight)
				.subscribe();
	}

	@Override
	public String getTitle() {
		return "Factuurlijst";
	}
}
