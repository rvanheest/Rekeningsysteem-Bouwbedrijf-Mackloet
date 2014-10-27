package org.rekeningsysteem.ui.header;

import rx.Observable;
import rx.Observer;

public class OmschrijvingController implements Observer<String> {

	private final OmschrijvingPane ui;
	private final Observable<String> model;

	public OmschrijvingController() {
		this(new OmschrijvingPane());
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

	@Override
	public void onCompleted() {
	}

	@Override
	public void onError(Throwable e) {
		e.printStackTrace();
	}

	@Override
	public void onNext(String omschrijving) {
		this.ui.setOmschrijving(omschrijving);
	}
}
