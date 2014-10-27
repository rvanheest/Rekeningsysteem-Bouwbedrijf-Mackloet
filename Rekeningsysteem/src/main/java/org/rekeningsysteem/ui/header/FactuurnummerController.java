package org.rekeningsysteem.ui.header;

import java.util.Optional;

import org.rekeningsysteem.ui.header.FactuurnummerPane.FactuurnummerType;

import rx.Observable;
import rx.Observer;

public class FactuurnummerController implements Observer<String> {

	private final FactuurnummerPane ui;
	private final Observable<Optional<String>> model;

	public FactuurnummerController(FactuurnummerType type) {
		this(new FactuurnummerPane(type));
	}

	public FactuurnummerController(FactuurnummerPane ui) {
		this.ui = ui;
		this.model = this.ui.getFactuurnummer();
	}

	public FactuurnummerPane getUI() {
		return this.ui;
	}

	public Observable<Optional<String>> getModel() {
		return this.model;
	}

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(String factuurnummer) {
		this.ui.setFactuurnummer(factuurnummer);
	}
}
