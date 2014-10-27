package org.rekeningsysteem.ui.aangenomen;

import java.util.Arrays;
import java.util.List;

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

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;
import org.rekeningsysteem.ui.list.ButtonCell;
import org.rekeningsysteem.ui.list.MoneyCell;

import rx.Observable;

public class AangenomenListPane extends Page {

	private final TableView<AangenomenModel> table = new TableView<>();
	private final ObservableList<AangenomenModel> data = FXCollections.observableArrayList();

	private final Button up = new Button();
	private final Button down = new Button();
	private final Button add = new Button();

	public AangenomenListPane() {
		super("Factuurlijst");

		this.up.setId("up-button");
		this.down.setId("down-button");
		this.add.setId("add-button");

		this.table.setPlaceholder(new Label("Geen items in deze lijst"));
		this.table.getColumns().addAll(this.initTableColumns());
		this.table.setEditable(false);
		this.table.setItems(this.data);
		this.table.setSortPolicy(param -> false);
		this.table.setTableMenuButtonVisible(false);

		HBox hb = new HBox(this.table, this.initNavigationPane());
		hb.setSpacing(20);

		this.getChildren().add(hb);

		Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.doOnNext(this.table::setPrefHeight)
				.subscribe();
	}

	private List<TableColumn<AangenomenModel, ?>> initTableColumns() {
		TableColumn<AangenomenModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<AangenomenModel, Double> loonCol = new TableColumn<>("Loon");
		TableColumn<AangenomenModel, Double> materiaalCol = new TableColumn<>("Materiaal");
		TableColumn<AangenomenModel, Boolean> deleteCol = new TableColumn<>();

		omschrCol.setMinWidth(300);
		loonCol.setMinWidth(100);
		materiaalCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		loonCol.setCellValueFactory(new PropertyValueFactory<>("loon"));
		materiaalCol.setCellValueFactory(new PropertyValueFactory<>("materiaal"));
		deleteCol.setCellValueFactory(par -> new SimpleBooleanProperty(par.getValue() != null));

		loonCol.setCellFactory(param -> new MoneyCell<>());
		materiaalCol.setCellFactory(param -> new MoneyCell<>());
		deleteCol.setCellFactory(param -> {
			Button button = new Button();
			ButtonCell<AangenomenModel> buttonCell = new ButtonCell<>(button);
			Observables.fromNodeEvents(button, ActionEvent.ACTION)
					.map(event -> buttonCell.getTableView().getItems().get(buttonCell.getIndex()))
					.doOnNext(this.data::remove)
					.subscribe();
			return buttonCell;
		});

		return Arrays.asList(omschrCol, loonCol, materiaalCol, deleteCol);
	}

	private VBox initNavigationPane() {
		VBox nav = new VBox();
		nav.setId("nav-pane");
		nav.setAlignment(Pos.CENTER);
		nav.setSpacing(15);
		nav.getChildren().addAll(this.up, this.down, this.add);

		Observable<Integer> selectedRow = Observables.fromProperty(this.table.getSelectionModel()
				.selectedIndexProperty())
				.map(Number::intValue);
		selectedRow.filter(i -> this.data.size() > 1)
				.filter(i -> i == 0)
				.doOnNext(i -> this.up.setDisable(true))
				.doOnNext(i -> this.down.setDisable(false))
				.subscribe();
		selectedRow.filter(i -> this.data.size() > 1)
				.filter(i -> i == Math.max(0, this.data.size() - 1))
				.doOnNext(i -> this.up.setDisable(false))
				.doOnNext(i -> this.down.setDisable(true))
				.subscribe();
		selectedRow.filter(i -> !this.data.isEmpty())
				.filter(i -> i > 0)
				.filter(i -> i < Math.max(0, this.data.size() - 1))
				.doOnNext(i -> this.up.setDisable(false))
				.doOnNext(i -> this.down.setDisable(false))
				.subscribe();
		selectedRow.filter(i -> i == -1 || this.data.isEmpty() || this.data.size() == 1)
				.doOnNext(i -> this.up.setDisable(true))
				.doOnNext(i -> this.down.setDisable(true))
				.subscribe();

		return nav;
	}

	public Observable<List<? extends AangenomenModel>> getData() {
		return Observables.fromObservableList(this.data);
	}

	public void setData(List<AangenomenModel> list) {
		this.data.clear();
		this.data.addAll(list);
	}

	public Observable<Integer> getUpButtonEvent() {
		return Observables.fromNodeEvents(this.up, ActionEvent.ACTION)
				.map(event -> this.table.getSelectionModel().getSelectedIndex());
	}

	public Observable<Integer> getDownButtonEvent() {
		return Observables.fromNodeEvents(this.down, ActionEvent.ACTION)
				.map(event -> this.table.getSelectionModel().getSelectedIndex());
	}

	public Observable<ActionEvent> getAddButtonEvent() {
		return Observables.fromNodeEvents(this.add, ActionEvent.ACTION);
	}

	public static class AangenomenModel {

		private String omschrijving;
		private double loon;
		private double materiaal;

		public AangenomenModel(String omschrijving, double loon, double materiaal) {
			this.omschrijving = omschrijving;
			this.loon = loon;
			this.materiaal = materiaal;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public void setOmschrijving(String omschrijving) {
			this.omschrijving = omschrijving;
		}

		public double getLoon() {
			return this.loon;
		}

		public void setLoon(double loon) {
			this.loon = loon;
		}

		public double getMateriaal() {
			return this.materiaal;
		}

		public void setMateriaal(double materiaal) {
			this.materiaal = materiaal;
		}
	}
}
