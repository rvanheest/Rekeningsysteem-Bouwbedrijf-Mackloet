package org.rekeningsysteem.ui.particulier2.loon;

import java.util.Currency;

import org.rekeningsysteem.data.particulier2.loon.InstantLoon2;
import org.rekeningsysteem.data.util.Geld;

import rx.Observable;

// TODO InstantLoonController
public class InstantLoonController2 {

	private final InstantLoonPane2 ui;
	private final Observable<InstantLoon2> model;

	public InstantLoonController2(Currency currency) {
		this(new InstantLoonPane2(currency));
	}

	public InstantLoonController2(Currency currency, InstantLoon2 input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setLoon(input.getLoon().getBedrag());
	}

	public InstantLoonController2(InstantLoonPane2 ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getOmschrijving(), ui.getLoon().map(Geld::new),
				ui.getLoonBtwPercentage(), InstantLoon2::new);
	}

	public InstantLoonPane2 getUI() {
		return this.ui;
	}

	public Observable<InstantLoon2> getModel() {
		return this.model;
	}
}
