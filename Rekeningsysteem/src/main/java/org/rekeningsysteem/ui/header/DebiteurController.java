package org.rekeningsysteem.ui.header;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.textfields.searchbox.AbstractSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.DebiteurSearchBox;
import org.rekeningsysteem.ui.textfields.searchbox.SearchBoxController;

import rx.Observable;
import rx.schedulers.Schedulers;

public class DebiteurController {

	private final DebiteurPane ui;
	private final Observable<Debiteur> model;
	private final Observable<Boolean> saveDebiteur;
	private final SearchBoxController<Debiteur> searchBoxController;

	public DebiteurController() {
		this(DebiteurPane::new);
	}

	public DebiteurController(Debiteur input) {
		this();
		this.ui.setNaam(input.getNaam());
		this.ui.setStraat(input.getStraat());
		this.ui.setNummer(input.getNummer());
		this.ui.setPostcode(input.getPostcode());
		this.ui.setPlaats(input.getPlaats());
		this.ui.setBtwNummer(input.getBtwNummer().orElse(""));
		this.ui.setSaveSelected(false);
	}

	public DebiteurController(Function<AbstractSearchBox<Debiteur>, DebiteurPane> uiFactory) {
		this.searchBoxController = new SearchBoxController<>(new DebiteurSearchBox());
		this.ui = uiFactory.apply(this.searchBoxController.getUI());
		this.saveDebiteur = this.ui.isSaveSelected();
		this.model = Observable.combineLatest(this.ui.getNaam(), this.ui.getStraat(),
				this.ui.getNummer(), this.ui.getPostcode(), this.ui.getPlaats(),
				this.ui.getBtwnummer(), Debiteur::new)
				.publish(debs -> debs.skip(1) // this extra publish needs to be done because
											  // otherwise the saveSelect(false) will always be
											  // overruled by the latest update
						.doOnNext(d -> this.ui.setSaveSelected(true))
						.switchMap(d -> debs));

		this.initSearchBox();
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

	private void initSearchBox() {
		try {
			Database database = Database.getInstance();
			DebiteurDBInteraction interaction = new DebiteurDBInteraction(database);

			AbstractSearchBox<Debiteur> naamSearchBox = this.searchBoxController.getUI();
			naamSearchBox.textProperty()
					.throttleWithTimeout(300, TimeUnit.MILLISECONDS)
					.publish(text -> {
						text.filter(s -> s.length() < 2)
								.observeOn(JavaFxScheduler.getInstance())
								.subscribe(s -> naamSearchBox.hideContextMenu());

						text.filter(s -> s.length() >= 2)
								.observeOn(Schedulers.io())
								.map(interaction::getWithNaam)
								.observeOn(JavaFxScheduler.getInstance())
								.subscribe(naamSearchBox::populateMenu);

						return text;
					})
					.subscribe();

			naamSearchBox.getSelectedItem()
					.subscribe(debiteur -> {
						this.ui.setNaam(debiteur.getNaam());
						this.ui.setStraat(debiteur.getStraat());
						this.ui.setNummer(debiteur.getNummer());
						this.ui.setPostcode(debiteur.getPostcode());
						this.ui.setPlaats(debiteur.getPlaats());
						this.ui.setBtwNummer(debiteur.getBtwNummer().orElse(""));

						naamSearchBox.clear();
						this.ui.setSaveSelected(false);
					});
		}
		catch (SQLException e) {
			ApplicationLogger.getInstance()
					.fatal("Database exception: propably the database was not found!", e);
		}
	}
}
