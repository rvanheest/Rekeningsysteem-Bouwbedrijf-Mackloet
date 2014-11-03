package org.rekeningsysteem.ui.mutaties;

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

public class MutatiesListPane extends Page {

	private final TableView<MutatiesModel> table = new TableView<>();
	private final ObservableList<MutatiesModel> data = FXCollections.observableArrayList();

	private final Button up = new Button();
	private final Button down = new Button();
	private final Button add = new Button();

	public MutatiesListPane() {
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

	private List<TableColumn<MutatiesModel, ?>> initTableColumns() {
		TableColumn<MutatiesModel, String> omschrCol = new TableColumn<>("Omschrijving");
		TableColumn<MutatiesModel, String> bonnrCol = new TableColumn<>("Bonnummer");
		TableColumn<MutatiesModel, Double> prijsCol = new TableColumn<>("Prijs");
		TableColumn<MutatiesModel, Boolean> deleteCol = new TableColumn<>();

		omschrCol.setMinWidth(300);
		bonnrCol.setMinWidth(100);
		prijsCol.setMinWidth(100);

		omschrCol.setCellValueFactory(new PropertyValueFactory<>("omschrijving"));
		bonnrCol.setCellValueFactory(new PropertyValueFactory<>("bonnummer"));
		prijsCol.setCellValueFactory(new PropertyValueFactory<>("prijs"));
		deleteCol.setCellValueFactory(par -> new SimpleBooleanProperty(par.getValue() != null));

		prijsCol.setCellFactory(param -> new MoneyCell<>());
		deleteCol.setCellFactory(param -> {
			Button button = new Button();
			ButtonCell<MutatiesModel> buttonCell = new ButtonCell<>(button);
			Observables.fromNodeEvents(button, ActionEvent.ACTION)
					.map(event -> buttonCell.getTableView().getItems().get(buttonCell.getIndex()))
					.doOnNext(this.data::remove)
					.subscribe();
			return buttonCell;
		});

		return Arrays.asList(omschrCol, bonnrCol, prijsCol, deleteCol);
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

	public Observable<List<? extends MutatiesModel>> getData() {
		return Observables.fromObservableList(this.data);
	}

	public void setData(List<MutatiesModel> list) {
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

	public static class MutatiesModel {

		private String omschrijving;
		private String bonnummer;
		private double prijs;

		public MutatiesModel(String omschrijving, String bonnummer, double prijs) {
			this.omschrijving = omschrijving;
			this.bonnummer = bonnummer;
			this.prijs = prijs;
		}

		public String getOmschrijving() {
			return this.omschrijving;
		}

		public void setOmschrijving(String omschrijving) {
			this.omschrijving = omschrijving;
		}

		public String getBonnummer() {
			return this.bonnummer;
		}

		public void setBonnummer(String bonnummer) {
			this.bonnummer = bonnummer;
		}

		public double getPrijs() {
			return this.prijs;
		}

		public void setPrijs(double prijs) {
			this.prijs = prijs;
		}
	}
}
