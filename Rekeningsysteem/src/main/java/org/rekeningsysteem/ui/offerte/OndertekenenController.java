package org.rekeningsysteem.ui.offerte;

import rx.Observable;
import rx.Observer;

public class OndertekenenController implements Observer<Boolean> {

	private final OndertekenenPane ui;
	private final Observable<Boolean> model;

	public OndertekenenController() {
		this(new OndertekenenPane());
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

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(Boolean ondertekenen) {
		this.ui.setOndertekenen(ondertekenen);
	}
}
