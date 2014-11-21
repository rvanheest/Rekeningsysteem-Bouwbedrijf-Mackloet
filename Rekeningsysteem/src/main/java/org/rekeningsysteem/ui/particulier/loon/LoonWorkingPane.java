package org.rekeningsysteem.ui.particulier.loon;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;
import org.rekeningsysteem.ui.WorkingPane;

public class LoonWorkingPane extends WorkingPane {

	public LoonWorkingPane(Page page) {
		super(page);

		Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.map(d -> Math.min(d, 700))
				.doOnNext(page::setPrefHeight)
				.subscribe();
	}

	@Override
	public String getTitle() {
		return "Loon";
	}
}
