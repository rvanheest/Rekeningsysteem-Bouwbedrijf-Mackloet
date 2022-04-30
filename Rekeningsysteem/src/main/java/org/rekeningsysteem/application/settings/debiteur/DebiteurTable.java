package org.rekeningsysteem.application.settings.debiteur;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.list.ButtonCell;

public class DebiteurTable extends VBox implements Disposable {

	private final TableView<DebiteurTableModel> table = new TableView<>();
	private final ObservableList<DebiteurTableModel> data = FXCollections.observableArrayList();

	private final Button add = new Button();
	private final Button modify = new Button();

	private final Function<DebiteurTableModel, Completable> dbDelete;
	private final CompositeDisposable disposable = new CompositeDisposable();

	public DebiteurTable(Function<DebiteurTableModel, Completable> dbDelete) {
		this.dbDelete = dbDelete;

		this.setId("debiteur-table");
		this.add.setId("add-button");
		this.modify.setId("edit-button");
		this.getStyleClass().add("page");

		this.table.setPlaceholder(new Label("Geen debiteuren gevonden"));
		this.table.getColumns().addAll(this.initTableColumns());
		this.table.setEditable(false);
		this.table.setItems(this.data);
		this.table.setSortPolicy(param -> false);
		this.table.setTableMenuButtonVisible(false);

		HBox hb = new HBox(this.table, this.initNavigationPane());
		hb.setSpacing(20);

		this.getChildren().add(hb);

		this.disposable.add(
			Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.subscribe(this.table::setPrefHeight)
		);
	}

	protected List<TableColumn<DebiteurTableModel, ?>> initTableColumns() {
		TableColumn<DebiteurTableModel, String> naamCol = new TableColumn<>("Naam");
		TableColumn<DebiteurTableModel, String> straatCol = new TableColumn<>("Straat");
		TableColumn<DebiteurTableModel, String> nummerCol = new TableColumn<>("Nummer");
		TableColumn<DebiteurTableModel, String> postcodeCol = new TableColumn<>("Postcode");
		TableColumn<DebiteurTableModel, String> plaatsCol = new TableColumn<>("Plaats");
		TableColumn<DebiteurTableModel, String> btwNummerCol = new TableColumn<>("Btw nummer");

		naamCol.setMinWidth(150);
		straatCol.setMinWidth(200);
		nummerCol.setMinWidth(25);
		postcodeCol.setMinWidth(60);
		plaatsCol.setMinWidth(200);
		btwNummerCol.setMinWidth(120);

		naamCol.setCellValueFactory(new PropertyValueFactory<>("naam"));
		straatCol.setCellValueFactory(new PropertyValueFactory<>("straat"));
		nummerCol.setCellValueFactory(new PropertyValueFactory<>("nummer"));
		postcodeCol.setCellValueFactory(new PropertyValueFactory<>("postcode"));
		plaatsCol.setCellValueFactory(new PropertyValueFactory<>("plaats"));
		btwNummerCol.setCellValueFactory(new PropertyValueFactory<>("btwNummer"));

		return Arrays.asList(naamCol, straatCol, nummerCol, postcodeCol, plaatsCol, btwNummerCol, this.getDeleteCol());
	}

	protected TableColumn<DebiteurTableModel, Boolean> getDeleteCol() {
		TableColumn<DebiteurTableModel, Boolean> deleteCol = new TableColumn<>();

		deleteCol.setCellValueFactory(par -> new SimpleBooleanProperty(par.getValue() != null));
		deleteCol.setCellFactory(param -> {
			Button button = new Button();
			ButtonCell<DebiteurTableModel> buttonCell = new ButtonCell<>(button);
			this.disposable.add(
				Observables.fromNodeEvents(button, ActionEvent.ACTION)
					.map(event -> buttonCell.getTableView().getItems().get(buttonCell.getIndex()))
					.observeOn(Schedulers.io())
					.flatMapSingle(model -> this.dbDelete.apply(model).toSingle(() -> model))
					.observeOn(JavaFxScheduler.getInstance())
					.subscribe(this.data::remove)
			);
			return buttonCell;
		});
		deleteCol.setMinWidth(50);
		deleteCol.setMaxWidth(50);
		deleteCol.setPrefWidth(50);

		return deleteCol;
	}

	public int getSelectedIndex() {
		return this.table.getSelectionModel().getSelectedIndex();
	}

	protected VBox initNavigationPane() {
		VBox nav = new VBox();
		nav.setId("nav-pane");
		nav.setAlignment(Pos.CENTER);
		nav.setSpacing(15);
		nav.getChildren().addAll(this.add, this.modify);

		this.disposable.add(
			Observables.fromProperty(this.table.getSelectionModel().selectedIndexProperty())
				.map(Number::intValue)
				.map(i -> this.data.isEmpty())
				.subscribe(this.modify::setDisable)
		);

		return nav;
	}

	public Observable<List<? extends DebiteurTableModel>> getData() {
		return Observables.fromObservableList(this.data);
	}

	public void setData(List<DebiteurTableModel> list) {
		this.data.clear();
		this.data.addAll(list);
	}

	public Observable<ActionEvent> getAddButtonEvent() {
		return Observables.fromNodeEvents(this.add, ActionEvent.ACTION);
	}

	public Observable<ActionEvent> getModifyButtonEvent() {
		return Observables.fromNodeEvents(this.modify, ActionEvent.ACTION);
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}

	public record DebiteurTableModel(
			Integer id,
			String naam,
			String straat,
			String nummer,
			String postcode,
			String plaats,
			String btwNummer
	) {}
}
