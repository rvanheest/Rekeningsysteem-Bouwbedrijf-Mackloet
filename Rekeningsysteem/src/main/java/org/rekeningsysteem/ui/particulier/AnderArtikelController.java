package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.util.Geld;

import rx.Observable;

@Deprecated
public class AnderArtikelController {

	private final AnderArtikelPane ui;
	private final Observable<AnderArtikel> model;

	public AnderArtikelController(Currency currency) {
		this(new AnderArtikelPane(currency));
	}

	public AnderArtikelController(Currency currency, AnderArtikel input) {
		this(currency);
		this.getUI().setOmschrijving(input.getOmschrijving());
		this.getUI().setPrijs(input.getMateriaal().getBedrag());
	}

	public AnderArtikelController(AnderArtikelPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(ui.getOmschrijving(), ui.getPrijs().map(Geld::new),
				ui.getBtwPercentage(), AnderArtikel::new);
	}

	public AnderArtikelPane getUI() {
		return this.ui;
	}

	public Observable<AnderArtikel> getModel() {
		return this.model;
	}
}
