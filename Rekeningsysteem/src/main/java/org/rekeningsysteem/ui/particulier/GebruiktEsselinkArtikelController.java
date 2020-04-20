package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.particulier.EsselinkArtikelToggle;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.EsselinkSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.SearchBoxController;

import rx.Observable;
import rx.schedulers.Schedulers;

public class GebruiktEsselinkArtikelController {

	private final ArtikellijstDBInteraction db;

	private final GebruiktEsselinkArtikelPane ui;
	private final Observable<GebruiktEsselinkArtikel> model;
	private final SearchBoxController<EsselinkArtikel> searchBoxController;

	public GebruiktEsselinkArtikelController(Currency currency, ArtikellijstDBInteraction db) {
		this(currency, db, factory -> new GebruiktEsselinkArtikelPane(currency, factory));
	}

	public GebruiktEsselinkArtikelController(Currency currency, ArtikellijstDBInteraction db,
			Function<AbstractSearchBox<EsselinkArtikel>, GebruiktEsselinkArtikelPane> uiFactory) {
		this.db = db;
		this.searchBoxController = new SearchBoxController<>(new EsselinkSearchBox(currency));
		this.ui = uiFactory.apply(this.searchBoxController.getUI());
		this.model = Observable.combineLatest(this.ui.getArtikel(), this.ui.getAantal(),
				this.ui.getBtwPercentage(), GebruiktEsselinkArtikel::new);

		this.initSearchBox();
	}

	public GebruiktEsselinkArtikelPane getUI() {
		return this.ui;
	}

	public Observable<GebruiktEsselinkArtikel> getModel() {
		return this.model;
	}

	private void initSearchBox() {
		AbstractSearchBox<EsselinkArtikel> searchField = this.searchBoxController.getUI();

		this.ui.getSelectedToggle()
				.publish(toggle -> {
					toggle.subscribe(t -> searchField.clear());

					Observable<Function<String, Observable<EsselinkArtikel>>> toggleFunction =
							toggle.map(t -> {
								if (t == EsselinkArtikelToggle.ARTIKELNUMMER)
									return this.db::getWithArtikelnummer;
								assert t == EsselinkArtikelToggle.OMSCHRIJVING;
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
										.onBackpressureBuffer()
										.observeOn(JavaFxScheduler.getInstance())
										.doOnNext(searchField::populateMenu)
										.flatMap(o -> o);
							});
				})
				.subscribe();
	}
}
