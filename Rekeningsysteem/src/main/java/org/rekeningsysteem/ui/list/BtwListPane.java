package org.rekeningsysteem.ui.list;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;
import org.rekeningsysteem.ui.WorkingPane;

public class BtwListPane extends WorkingPane {

	// TODO rename since Btw
	public BtwListPane(Page listPane) {
		super(listPane);

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
