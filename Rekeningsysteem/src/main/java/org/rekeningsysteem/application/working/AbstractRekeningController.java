package org.rekeningsysteem.application.working;

import org.rekeningsysteem.data.util.AbstractRekening;

import rx.Observable;

public abstract class AbstractRekeningController {

	private final RekeningSplitPane ui;

	public AbstractRekeningController(RekeningSplitPane ui) {
		this.ui = ui;
	}

	public RekeningSplitPane getUI() {
		return this.ui;
	}

	public abstract Observable<? extends AbstractRekening> getModel();

	public abstract void initFactuurnummer();
}
