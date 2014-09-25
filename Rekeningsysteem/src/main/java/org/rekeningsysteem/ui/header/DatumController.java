package org.rekeningsysteem.ui.header;

import java.time.LocalDate;

import com.google.inject.Inject;

import rx.Observable;
import rx.Observer;

public class DatumController implements Observer<LocalDate> {

	private final DatumPane ui;
	private final Observable<LocalDate> model;

	@Inject
	public DatumController(DatumPane ui) {
		this.ui = ui;
		this.model = this.ui.getDatum();
	}

	public DatumPane getUI() {
		return this.ui;
	}

	public Observable<LocalDate> getModel() {
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
	public void onNext(LocalDate datum) {
		this.ui.setDatum(datum);
	}
}
