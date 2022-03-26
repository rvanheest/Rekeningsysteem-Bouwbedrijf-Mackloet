package org.rekeningsysteem.ui.header;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.DebiteurSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.SearchBoxController;

public class DebiteurController {

	private final DebiteurPane ui;
	private final Observable<Debiteur> model;
	private final Observable<Boolean> saveDebiteur;
	private final SearchBoxController<Debiteur> searchBoxController;

	public DebiteurController(DebiteurDBInteraction db) {
		this(DebiteurPane::new, db);
	}

	public DebiteurController(Debiteur input, DebiteurDBInteraction db) {
		this(db);
		this.ui.setNaam(input.getNaam());
		this.ui.setStraat(input.getStraat());
		this.ui.setNummer(input.getNummer());
		this.ui.setPostcode(input.getPostcode());
		this.ui.setPlaats(input.getPlaats());
		this.ui.setBtwNummer(input.getBtwNummer().orElse(""));
		this.ui.setSaveSelected(false);
	}

	public DebiteurController(Function<AbstractSearchBox<Debiteur>, DebiteurPane> uiFactory, DebiteurDBInteraction db) {
		DebiteurSearchBox ui = new DebiteurSearchBox();
		this.searchBoxController = new SearchBoxController<>(ui);
		this.ui = uiFactory.apply(this.searchBoxController.getUI());
		this.saveDebiteur = this.ui.isSaveSelected();
		this.model = Observable.combineLatest(
				this.ui.getNaam(),
				this.ui.getStraat(),
				this.ui.getNummer(),
				this.ui.getPostcode(),
				this.ui.getPlaats(),
				this.ui.getBtwnummer(),
				Debiteur::new)
			.publish(debs -> {
				debs.skip(1).subscribe(d -> this.ui.setSaveSelected(true));
				return debs;
			});

		ui.textProperty()
			.throttleWithTimeout(300, TimeUnit.MILLISECONDS, JavaFxScheduler.getInstance())
			.publish(text -> text.filter(s -> s.length() < 2)
				.doOnNext(s -> ui.hideContextMenu())
				.switchMap(s -> text)
				.filter(s -> s.length() >= 2)
				.observeOn(Schedulers.io())
				.map(db::getWithNaam)
				.observeOn(JavaFxScheduler.getInstance())
				.doOnNext(ui::populateMenu))
			.subscribe();

		ui.getSelectedItem()
			.subscribe(debiteur -> {
				this.ui.setNaam(debiteur.getNaam());
				this.ui.setStraat(debiteur.getStraat());
				this.ui.setNummer(debiteur.getNummer());
				this.ui.setPostcode(debiteur.getPostcode());
				this.ui.setPlaats(debiteur.getPlaats());
				this.ui.setBtwNummer(debiteur.getBtwNummer().orElse(""));

				ui.clear();
				this.ui.setSaveSelected(false);
			});
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
}
