package org.rekeningsysteem.ui.particulier;

import java.util.Currency;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.EsselinkSearchBox;

public class GebruiktEsselinkArtikelController implements Disposable {

	private final ArtikellijstDBInteraction db;

	private final GebruiktEsselinkArtikelPane ui;
	private final Observable<GebruiktEsselinkArtikel> model;
	private final EsselinkSearchBox searchBoxUi;

	private final CompositeDisposable disposable = new CompositeDisposable();

	public GebruiktEsselinkArtikelController(Currency currency, ArtikellijstDBInteraction db) {
		this(currency, db, factory -> new GebruiktEsselinkArtikelPane(currency, factory));
	}

	public GebruiktEsselinkArtikelController(Currency currency, ArtikellijstDBInteraction db, Function<AbstractSearchBox<EsselinkArtikel>, GebruiktEsselinkArtikelPane> uiFactory) {
		this.db = db;
		this.searchBoxUi = new EsselinkSearchBox(currency);
		this.ui = uiFactory.apply(this.searchBoxUi);
		this.model = Observable.combineLatest(
			this.ui.getArtikel(),
			this.ui.getAantal(),
			this.ui.getBtwPercentage(),
			GebruiktEsselinkArtikel::new
		);

		this.disposable.addAll(this.ui, this.searchBoxUi);

		this.initSearchBox();
	}

	public GebruiktEsselinkArtikelPane getUI() {
		return this.ui;
	}

	public Observable<GebruiktEsselinkArtikel> getModel() {
		return this.model;
	}

	public void setBtwPercentage(BtwPercentage btwPercentage) {
		this.ui.setBtwPercentage(btwPercentage);
	}

	private void initSearchBox() {
		this.ui.getSelectedToggle()
			.publish(toggle -> {
				this.disposable.add(
				toggle.subscribe(t -> this.searchBoxUi.clear())
				);

				Observable<Function<String, Observable<EsselinkArtikel>>> toggleFunction =
					toggle.map(t -> {
						if (t == EsselinkArtikelToggle.ARTIKELNUMMER)
							return this.db::getWithArtikelnummer;
						assert t == EsselinkArtikelToggle.OMSCHRIJVING;
						return this.db::getWithOmschrijving;
					});

				return this.searchBoxUi.textProperty()
					.throttleWithTimeout(300, TimeUnit.MILLISECONDS)
					.publish(text -> {
						this.disposable.add(
						text.filter(s -> s.length() < 4)
							.observeOn(JavaFxScheduler.getInstance())
							.subscribe(s -> this.searchBoxUi.hideContextMenu())
						);

						return text.filter(s -> s.length() >= 4)
							.observeOn(Schedulers.io())
							.withLatestFrom(toggleFunction, (s, f) -> f.apply(s))
							.toFlowable(BackpressureStrategy.BUFFER)
							.observeOn(JavaFxScheduler.getInstance())
							.doOnNext(((AbstractSearchBox<EsselinkArtikel>) this.searchBoxUi)::populateMenu)
							.toObservable()
							.flatMap(o -> o);
					});
			})
			.subscribe();
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}
