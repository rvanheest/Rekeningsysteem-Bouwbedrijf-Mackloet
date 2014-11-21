package org.rekeningsysteem.ui.particulier.loon;

import java.util.Currency;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.InstantLoon;

import rx.Observable;

public class InstantLoonController {

	private final InstantLoonPane ui;
	private final Observable<InstantLoon> model;

	public InstantLoonController(Currency currency) {
		this(new InstantLoonPane(currency));
	}

	public InstantLoonController(Currency currency, InstantLoon input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setLoon(input.getLoon().getBedrag());
	}

	public InstantLoonController(InstantLoonPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getOmschrijving(), ui.getLoon().map(Geld::new), InstantLoon::new);
	}

	public InstantLoonPane getUI() {
		return this.ui;
	}

	public Observable<InstantLoon> getModel() {
		return this.model;
	}
}
