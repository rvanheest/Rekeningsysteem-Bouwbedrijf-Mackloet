package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;

import rx.Observable;

public class GebruiktEsselinkArtikelController {

	private final GebruiktEsselinkArtikelPane ui;
	private final Observable<GebruiktEsselinkArtikel> model;

	public GebruiktEsselinkArtikelController(Currency currency) {
		this(new GebruiktEsselinkArtikelPane(currency));
	}

	public GebruiktEsselinkArtikelController(GebruiktEsselinkArtikelPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getArtikel(), ui.getAantal(),
				GebruiktEsselinkArtikel::new);
	}

	public GebruiktEsselinkArtikelPane getUI() {
		return this.ui;
	}

	public Observable<GebruiktEsselinkArtikel> getModel() {
		return this.model;
	}
}
