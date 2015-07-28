package org.rekeningsysteem.ui.list;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;
import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.btw.BtwPane;

public class BtwListPane extends WorkingPane {

	public BtwListPane(Page listPane, BtwPane btwPane) {
		super(btwPane, listPane);

		Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.map(d -> Math.min(d, 700))
				.subscribe(listPane::setPrefHeight);
	}

	@Override
	public String getTitle() {
		return "BTW & Factuurlijst";
	}
}
