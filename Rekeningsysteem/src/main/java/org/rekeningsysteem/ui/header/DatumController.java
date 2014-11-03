package org.rekeningsysteem.ui.header;

import java.time.LocalDate;

import rx.Observable;

public class DatumController {

	private final DatumPane ui;
	private final Observable<LocalDate> model;

	public DatumController() {
		this(new DatumPane());
	}

	public DatumController(LocalDate input) {
		this();
		this.ui.setDatum(input);
	}

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
}
