package org.rekeningsysteem.ui.header;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.DebiteurSearchBox;

public class DebiteurController implements Disposable {

	private final DebiteurPane ui;
	private final Observable<Debiteur> model;
	private final Observable<Boolean> saveDebiteur;
	private final CompositeDisposable disposables = new CompositeDisposable();

	public DebiteurController(DebiteurDBInteraction db) {
		this(DebiteurPane::new, db);
	}

	public DebiteurController(Debiteur input, DebiteurDBInteraction db) {
		this(db);
		this.ui.setNaam(input.naam());
		this.ui.setStraat(input.straat());
		this.ui.setNummer(input.nummer());
		this.ui.setPostcode(input.postcode());
		this.ui.setPlaats(input.plaats());
		this.ui.setBtwNummer(input.btwNummer().orElse(""));
		this.ui.setSaveSelected(false);
	}

	public DebiteurController(Function<AbstractSearchBox<Debiteur>, DebiteurPane> uiFactory, DebiteurDBInteraction db) {
		DebiteurSearchBox searchBox = new DebiteurSearchBox();
		this.ui = uiFactory.apply(searchBox);
		this.saveDebiteur = this.ui.isSaveSelected();
		this.model = this.getDebiteurObservable();

		this.disposables.addAll(
			this.model.skip(1).subscribe(d -> this.ui.setSaveSelected(true)),

			searchBox.textProperty()
				.throttleWithTimeout(300, TimeUnit.MILLISECONDS, JavaFxScheduler.getInstance())
				.flatMapMaybe(s -> s.length() < 2 ? Maybe.fromAction(searchBox::hideContextMenu) : Maybe.just(s))
				.observeOn(Schedulers.io())
				.map(db::getWithNaam)
				.observeOn(JavaFxScheduler.getInstance())
				.subscribe(searchBox::populateMenu),

			searchBox.getSelectedItem().subscribe(this.setDebiteur(searchBox)),

			searchBox
		);
	}

	private Observable<Debiteur> getDebiteurObservable() {
		return Observable.combineLatest(
			this.ui.getNaam(),
			this.ui.getStraat(),
			this.ui.getNummer(),
			this.ui.getPostcode(),
			this.ui.getPlaats(),
			this.ui.getBtwnummer(),
			Debiteur::new
		);
	}

	private Consumer<Debiteur> setDebiteur(AbstractSearchBox<Debiteur> searchBox) {
		return debiteur -> {
			this.ui.setNaam(debiteur.naam());
			this.ui.setStraat(debiteur.straat());
			this.ui.setNummer(debiteur.nummer());
			this.ui.setPostcode(debiteur.postcode());
			this.ui.setPlaats(debiteur.plaats());
			this.ui.setBtwNummer(debiteur.btwNummer().orElse(""));

			searchBox.clear();
			this.ui.setSaveSelected(false);
		};
	}

	public DebiteurPane getUI() {
		return this.ui;
	}

	public Observable<Debiteur> getModel() {
		return this.model;
	}

	public Observable<Boolean> isSaveSelected() {
		return this.saveDebiteur;
	}

	@Override
	public boolean isDisposed() {
		return this.disposables.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposables.dispose();
	}
}
