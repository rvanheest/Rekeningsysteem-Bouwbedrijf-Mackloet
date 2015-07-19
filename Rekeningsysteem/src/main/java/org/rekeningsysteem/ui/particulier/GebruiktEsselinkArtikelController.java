package org.rekeningsysteem.ui.particulier;

import java.sql.SQLException;
import java.util.Currency;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.EsselinkSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.SearchBoxController;

import rx.Observable;
import rx.schedulers.Schedulers;

public class GebruiktEsselinkArtikelController {

	private final GebruiktEsselinkArtikelPane ui;
	private final Observable<GebruiktEsselinkArtikel> model;
	private final SearchBoxController<EsselinkArtikel> searchBoxController;

	public GebruiktEsselinkArtikelController(Currency currency) {
		this(currency, factory -> new GebruiktEsselinkArtikelPane(currency, factory));
	}

	public GebruiktEsselinkArtikelController(Currency currency,
			Function<AbstractSearchBox<EsselinkArtikel>, GebruiktEsselinkArtikelPane> uiFactory) {
		this.searchBoxController = new SearchBoxController<>(new EsselinkSearchBox(currency));
		this.ui = uiFactory.apply(this.searchBoxController.getUI());
		this.model = Observable.combineLatest(this.ui.getArtikel(), this.ui.getAantal(),
				GebruiktEsselinkArtikel::new);

		this.initSearchBox();
	}

	public GebruiktEsselinkArtikelPane getUI() {
		return this.ui;
	}

	public Observable<GebruiktEsselinkArtikel> getModel() {
		return this.model;
	}

	private void initSearchBox() {
		try {
			Database database = Database.getInstance();
			ArtikellijstDBInteraction interaction = new ArtikellijstDBInteraction(database);

			AbstractSearchBox<EsselinkArtikel> searchField = this.searchBoxController.getUI();

			this.ui.getSelectedToggle()
					.publish(toggle -> {
						toggle.subscribe(t -> searchField.clear());

						Observable<Function<String, Observable<EsselinkArtikel>>> toggleFunction =
								toggle.map(t -> {
									if (t == EsselinkArtikelToggle.ARTIKELNUMMER)
										return interaction::getWithArtikelnummer;
									assert t == EsselinkArtikelToggle.OMSCHRIJVING;
									return interaction::getWithOmschrijving;
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
		catch (SQLException e) {
			ApplicationLogger.getInstance()
					.fatal("Database exception: propably the database was not found!", e);
		}
	}
}
