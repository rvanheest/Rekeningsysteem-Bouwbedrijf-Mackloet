package org.rekeningsysteem.ui.header;

import io.reactivex.rxjava3.core.Observable;

public class OmschrijvingController {

	private final OmschrijvingPane ui;
	private final Observable<String> model;

	public OmschrijvingController() {
		this(new OmschrijvingPane());
	}

	public OmschrijvingController(String input) {
		this();
		this.ui.setOmschrijving(input);
	}

	public OmschrijvingController(OmschrijvingPane ui) {
		this.ui = ui;
		this.model = this.ui.getOmschrijving();
	}

	public OmschrijvingPane getUI() {
		return this.ui;
	}

	public Observable<String> getModel() {
		return this.model;
	}
}
