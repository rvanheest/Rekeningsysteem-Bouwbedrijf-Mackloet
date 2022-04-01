package org.rekeningsysteem.ui.header;

import io.reactivex.rxjava3.core.Observable;

public class OmschrijvingController {

	private final OmschrijvingPane ui;
	private final Observable<String> model;

	public OmschrijvingController() {
		this.ui = new OmschrijvingPane();
		this.model = this.ui.getOmschrijving();
	}

	public OmschrijvingController(String input) {
		this();
		this.ui.setOmschrijving(input);
	}

	public OmschrijvingPane getUI() {
		return this.ui;
	}

	public Observable<String> getModel() {
		return this.model;
	}
}
