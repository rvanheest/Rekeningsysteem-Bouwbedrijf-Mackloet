package org.rekeningsysteem.ui.particulier2;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.util.Geld;

import rx.Observable;

// TODO AnderArtikelController
public class ParticulierArtikelImplController {

	private final ParticulierArtikelImplPane ui;
	private final Observable<ParticulierArtikel2Impl> model;

	public ParticulierArtikelImplController(Currency currency) {
		this(new ParticulierArtikelImplPane(currency));
	}

	public ParticulierArtikelImplController(Currency currency, ParticulierArtikel2Impl input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setPrijs(input.getMateriaal().getBedrag());
	}

	public ParticulierArtikelImplController(ParticulierArtikelImplPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getOmschrijving(), ui.getPrijs().map(Geld::new),
				ui.getBtwPercentage(), ParticulierArtikel2Impl::new);
	}

	public ParticulierArtikelImplPane getUI() {
		return this.ui;
	}

	public Observable<ParticulierArtikel2Impl> getModel() {
		return this.model;
	}
}
