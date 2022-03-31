package org.rekeningsysteem.ui.list;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;
import org.rekeningsysteem.ui.WorkingPane;

public class BtwListPane extends WorkingPane implements Disposable {

	private final CompositeDisposable disposable = new CompositeDisposable();

	// TODO rename since Btw
	public BtwListPane(Page listPane) {
		super("BTW & Factuurlijst", listPane);

		this.disposable.add(
			Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.map(d -> Math.min(d, 700))
				.subscribe(listPane::setPrefHeight)
		);
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}
