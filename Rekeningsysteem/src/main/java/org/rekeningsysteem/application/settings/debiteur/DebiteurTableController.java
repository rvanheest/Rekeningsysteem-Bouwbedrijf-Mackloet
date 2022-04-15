package org.rekeningsysteem.application.settings.debiteur;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.settings.debiteur.DebiteurTable.DebiteurTableModel;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.util.OptionalUtils;

public class DebiteurTableController implements Disposable {

	private final DebiteurTable ui;
	private final Observable<List<Debiteur>> model;

	private final CompositeDisposable disposable = new CompositeDisposable();

	public DebiteurTableController(DebiteurDBInteraction db) {
		this(new DebiteurTable(debModel -> db.deleteDebiteur(uiToModel(debModel))), db);
	}

	public DebiteurTableController(DebiteurTable ui, DebiteurDBInteraction db) {
		this.ui = ui;
		this.model = this.ui.getData().map(this::uiToModel);

		this.disposable.addAll(
			db.getAll().toList().map(this::modelToUI).subscribe(this.ui::setData),

			this.ui.getAddButtonEvent()
				.map(event -> new DebiteurItemPaneController())
				.doOnNext(DebiteurItemPaneController::showModalMessage)
				.flatMapMaybe(DebiteurItemPaneController::getModel)
				.doOnNext(optDebiteur -> Main.getMain().hideModalMessage())
				.flatMapMaybe(Maybe::fromOptional)
				.observeOn(Schedulers.io())
				.flatMap(db::addAndGetDebiteur)
				.flatMapMaybe(deb -> this.model.firstElement().doOnSuccess(list -> list.add(deb)))
				.map(this::modelToUI)
				.observeOn(JavaFxScheduler.getInstance())
				.subscribe(this.ui::setData),

			this.ui.getModifyButtonEvent()
				.map(event -> this.ui.getSelectedIndex())
				.flatMapMaybe(index -> this.model.firstElement()
					.map(list -> list.get(index))
					.flatMap(old -> {
						DebiteurItemPaneController controller = new DebiteurItemPaneController(old);
						controller.showModalMessage();
						return controller.getModel()
							.doOnSuccess(optItem -> Main.getMain().hideModalMessage())
							.flatMap(Maybe::fromOptional)
							.observeOn(Schedulers.io())
							.flatMapSingle(newDebiteur -> db.updateDebiteur(old, newDebiteur).toSingle(() -> newDebiteur));
					})
					.flatMap(deb -> this.model.firstElement().doOnSuccess(list -> list.set(index, deb))))
				.map(this::modelToUI)
				.observeOn(JavaFxScheduler.getInstance())
				.subscribe(this.ui::setData),

			this.ui
		);
	}

	public Observable<List<Debiteur>> getModel() {
		return this.model;
	}

	public DebiteurTable getUI() {
		return this.ui;
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}

	private List<DebiteurTableModel> modelToUI(List<Debiteur> list) {
		return list.stream().map(DebiteurTableController::modelToUI).collect(Collectors.toList());
	}

	private static DebiteurTableModel modelToUI(Debiteur debiteur) {
		return new DebiteurTableModel(
			debiteur.debiteurID().orElse(null),
			debiteur.naam(),
			debiteur.straat(),
			debiteur.nummer(),
			debiteur.postcode(),
			debiteur.plaats(),
			debiteur.btwNummer().orElse("")
		);
	}

	private List<Debiteur> uiToModel(List<? extends DebiteurTableModel> list) {
		return list.stream().map(DebiteurTableController::uiToModel).collect(Collectors.toList());
	}

	private static Debiteur uiToModel(DebiteurTableModel model) {
		return new Debiteur(
			Optional.ofNullable(model.getId()),
			model.getNaam(),
			model.getStraat(),
			model.getNummer(),
			model.getPostcode(),
			model.getPlaats(),
			OptionalUtils.fromString(model.getBtwNummer())
		);
	}
}
