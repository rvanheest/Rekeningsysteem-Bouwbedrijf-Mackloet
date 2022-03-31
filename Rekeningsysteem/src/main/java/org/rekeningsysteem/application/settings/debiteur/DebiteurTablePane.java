package org.rekeningsysteem.application.settings.debiteur;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.rxjavafx.Observables;

public class DebiteurTablePane extends VBox implements Disposable {

	private final CompositeDisposable disposable = new CompositeDisposable();

	public DebiteurTablePane(DebiteurTable table) {
		this.getStyleClass().addAll("working-pane", "page");

		Label header = new Label("Debiteur instellingen");
		header.setId("title");

		this.getChildren().addAll(header, table);

		this.disposable.addAll(
			Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.map(d -> Math.min(d, 700))
				.subscribe(table::setPrefHeight),

			table
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
