package org.rekeningsysteem.ui.list;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.aangenomen.AangenomenListPane;
import org.rekeningsysteem.ui.btw.BtwPane;

public class ListPane extends WorkingPane {

	public ListPane(AangenomenListPane listPane, BtwPane btwPane) {
		super(btwPane, listPane);

		Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.map(d -> Math.min(d, 700))
				.doOnNext(listPane::setPrefHeight)
				.subscribe();
	}

	@Override
	public String getTitle() {
		return "BTW & Factuurlijst";
	}
}
