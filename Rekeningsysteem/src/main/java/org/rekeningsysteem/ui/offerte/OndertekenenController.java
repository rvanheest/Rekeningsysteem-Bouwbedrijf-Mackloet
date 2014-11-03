package org.rekeningsysteem.ui.offerte;

import rx.Observable;

public class OndertekenenController {

	private final OndertekenenPane ui;
	private final Observable<Boolean> model;

	public OndertekenenController() {
		this(new OndertekenenPane());
	}

	public OndertekenenController(boolean input) {
		this();
		this.ui.setOndertekenen(input);
	}

	public OndertekenenController(OndertekenenPane ui) {
		this.ui = ui;
		this.model = this.ui.isOndertekenen();
	}

	public OndertekenenPane getUI() {
		return this.ui;
	}

	public Observable<Boolean> getModel() {
		return this.model;
	}
}
