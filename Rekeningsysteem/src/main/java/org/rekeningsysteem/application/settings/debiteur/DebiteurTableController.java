package org.rekeningsysteem.application.settings.debiteur;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.application.settings.debiteur.DebiteurTable.DebiteurTableModel;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;

import rx.Observable;
import rx.schedulers.Schedulers;

public class DebiteurTableController {

	private final DebiteurTable ui;
	private final Observable<List<Debiteur>> model;

	public DebiteurTableController(DebiteurDBInteraction db) {
		this(new DebiteurTable(debModel -> db.deleteDebiteur(uiToModel(debModel))), db);
	}

	public DebiteurTableController(DebiteurTable ui, DebiteurDBInteraction db) {
		this.ui = ui;
		this.model = this.ui.getData().map(this::uiToModel);
		db.getAll().toList().map(this::modelToUI).subscribe(this.ui::setData);

		this.ui.getAddButtonEvent()
				.map(event -> new DebiteurItemPaneController())
				.doOnNext(controller -> Main.getMain().showModalMessage(controller.getUI()))
				.flatMap(controller -> controller.getModel())
				.doOnNext(optItem -> Main.getMain().hideModalMessage())
				.filter(Optional::isPresent)
				.map(Optional::get)
				.observeOn(Schedulers.io())
				.flatMap(db::addAndGetDebiteur)
				.flatMap(deb -> this.model.first().doOnNext(list -> list.add(deb)))
				.map(this::modelToUI)
				.observeOn(JavaFxScheduler.getInstance())
				.subscribe(this.ui::setData);

		this.ui.getModifyButtonEvent()
				.map(event -> this.ui.getSelectedIndex())
				.<List<Debiteur>> flatMap(index -> this.model.first()
						.map(list -> list.get(index))
						.<Debiteur> flatMap(old -> {
							DebiteurItemPaneController controller =
									new DebiteurItemPaneController(old);
							Main.getMain().showModalMessage(controller.getUI());
							return controller.getModel()
									.doOnNext(optItem -> Main.getMain().hideModalMessage())
									.filter(Optional::isPresent)
									.map(Optional::get)
									.observeOn(Schedulers.io())
									.flatMap(newDebiteur -> db.updateDebiteur(old, newDebiteur),
											(newDebiteur, i) -> newDebiteur);
						})
						.flatMap(deb -> this.model.first().doOnNext(list -> list.set(index, deb))))
				.map(this::modelToUI)
				.observeOn(JavaFxScheduler.getInstance())
				.subscribe(this.ui::setData);
	}

	public Observable<List<Debiteur>> getModel() {
		return this.model;
	}

	public DebiteurTable getUI() {
		return this.ui;
	}

	private List<DebiteurTableModel> modelToUI(List<Debiteur> list) {
		return list.stream().map(DebiteurTableController::modelToUI).collect(Collectors.toList());
	}

	private static DebiteurTableModel modelToUI(Debiteur debiteur) {
		return new DebiteurTableModel(debiteur.getDebiteurID().orElse(null), debiteur.getNaam(),
				debiteur.getStraat(), debiteur.getNummer(), debiteur.getPostcode(),
				debiteur.getPlaats(), debiteur.getBtwNummer().orElse(""));
	}

	private List<Debiteur> uiToModel(List<? extends DebiteurTableModel> list) {
		return list.stream().map(DebiteurTableController::uiToModel).collect(Collectors.toList());
	}

	private static Debiteur uiToModel(DebiteurTableModel model) {
		Integer id = model.getId();
		Optional<Integer> debiteurID = Optional.ofNullable(id);
		String btw = model.getBtwNummer();
		Optional<String> btwNummer = "".equals(btw) ? Optional.empty() : Optional.ofNullable(btw);
		return new Debiteur(debiteurID, model.getNaam(), model.getStraat(), model.getNummer(),
				model.getPostcode(), model.getPlaats(), btwNummer);
	}
}
