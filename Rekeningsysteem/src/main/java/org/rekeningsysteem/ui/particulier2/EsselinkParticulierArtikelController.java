package org.rekeningsysteem.ui.particulier2;

import java.util.Currency;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.particulier2.EsselinkArtikelToggle2;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.EsselinkSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.SearchBoxController;

import rx.Observable;
import rx.schedulers.Schedulers;

public class EsselinkParticulierArtikelController {

	private final ArtikellijstDBInteraction db;

	private final EsselinkParticulierArtikelPane ui;
	private final Observable<EsselinkParticulierArtikel> model;
	private final SearchBoxController<EsselinkArtikel> searchBoxController;

	public EsselinkParticulierArtikelController(Currency currency, ArtikellijstDBInteraction db) {
		this(currency, db, factory -> new EsselinkParticulierArtikelPane(currency, factory));
	}

	public EsselinkParticulierArtikelController(Currency currency, ArtikellijstDBInteraction db,
			Function<AbstractSearchBox<EsselinkArtikel>, EsselinkParticulierArtikelPane> uiFactory) {
		this.db = db;
		this.searchBoxController = new SearchBoxController<>(new EsselinkSearchBox(currency));
		this.ui = uiFactory.apply(this.searchBoxController.getUI());
		this.model = Observable.combineLatest(this.ui.getArtikel(), this.ui.getAantal(),
				this.ui.getBtwPercentage(), EsselinkParticulierArtikel::new);

		this.initSearchBox();
	}

	public EsselinkParticulierArtikelPane getUI() {
		return this.ui;
	}

	public Observable<EsselinkParticulierArtikel> getModel() {
		return this.model;
	}

	private void initSearchBox() {
		AbstractSearchBox<EsselinkArtikel> searchField = this.searchBoxController.getUI();

		this.ui.getSelectedToggle()
				.publish(toggle -> {
					toggle.subscribe(t -> searchField.clear());

					Observable<Function<String, Observable<EsselinkArtikel>>> toggleFunction =
							toggle.map(t -> {
								if (t == EsselinkArtikelToggle2.ARTIKELNUMMER)
									return this.db::getWithArtikelnummer;
								assert t == EsselinkArtikelToggle2.OMSCHRIJVING;
								return this.db::getWithOmschrijving;
							});

					return searchField.textProperty()
							.throttleWithTimeout(300, TimeUnit.MILLISECONDS)
							.<EsselinkArtikel> publish(text -> {
								text.filter(s -> s.length() < 4)
										.observeOn(JavaFxScheduler.getInstance())
										.subscribe(s -> searchField.hideContextMenu());

								return text.filter(s -> s.length() >= 4)
										.observeOn(Schedulers.io())
										.withLatestFrom(toggleFunction, (s, f) -> f.apply(s))
										.observeOn(JavaFxScheduler.getInstance())
										.doOnNext(searchField::populateMenu)
										.flatMap(o -> o);
							});
				})
				.subscribe();
	}
}
