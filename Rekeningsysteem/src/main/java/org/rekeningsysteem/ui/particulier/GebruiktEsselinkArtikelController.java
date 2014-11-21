package org.rekeningsysteem.ui.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;

import rx.Observable;

public class GebruiktEsselinkArtikelController {

	private final GebruiktEsselinkArtikelPane ui;
	private final Observable<GebruiktEsselinkArtikel> model;

	public GebruiktEsselinkArtikelController(Currency currency) {
		this(new GebruiktEsselinkArtikelPane(currency));
	}

	public GebruiktEsselinkArtikelController(Currency currency, GebruiktEsselinkArtikel input) {
		this(currency);
		EsselinkArtikel artikel = input.getArtikel();
		this.getUI().setArtikelnummer(artikel.getArtikelNummer());
		this.getUI().setOmschrijving(artikel.getOmschrijving());
		this.getUI().setPrijsPer(artikel.getPrijsPer());
		this.getUI().setEenheid(artikel.getEenheid());
		this.getUI().setVerkoopPrijs(artikel.getVerkoopPrijs().getBedrag());
		this.getUI().setAantal(input.getAantal());
	}

	public GebruiktEsselinkArtikelController(GebruiktEsselinkArtikelPane ui) {
		this.ui = ui;
		this.model = Observable.combineLatest(
				Observable.combineLatest(ui.getArtikelnummer(),
						ui.getOmschrijving(),
						ui.getPrijsPer(),
						ui.getEenheid(),
						ui.getVerkoopPrijs().map(Geld::new), EsselinkArtikel::new),
				ui.getAantal(), GebruiktEsselinkArtikel::new);
	}

	public GebruiktEsselinkArtikelPane getUI() {
		return this.ui;
	}

	public Observable<GebruiktEsselinkArtikel> getModel() {
		return this.model;
	}
}
