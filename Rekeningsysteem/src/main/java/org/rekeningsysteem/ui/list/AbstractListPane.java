package org.rekeningsysteem.ui.list;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

import rx.Observable;

public abstract class AbstractListPane<T> extends Page {

	private final TableView<T> table = new TableView<>();
	private final ObservableList<T> data = FXCollections.observableArrayList();

	private final Button up = new Button();
	private final Button down = new Button();
	private final Button add = new Button();

	public AbstractListPane(String title) {
		super(title);

		this.up.setId("up-button");
		this.down.setId("down-button");
		this.add.setId("add-button");

		this.table.setPlaceholder(new Label("Geen items in deze lijst"));
		this.table.getColumns().addAll(this.initTableColumns());
		this.table.setEditable(false);
		this.table.setItems(this.data);
		this.table.setSortPolicy(param -> false);
		this.table.setTableMenuButtonVisible(false);

		this.getChildren().add(new HBox(20, this.table, this.initNavigationPane()));

		Observables.fromProperty(this.heightProperty())
				.map(Number::doubleValue)
				.doOnNext(this.table::setPrefHeight)
				.subscribe();
	}

	protected abstract List<TableColumn<T, ?>> initTableColumns();

	protected TableColumn<T, Boolean> getDeleteCol() {
		TableColumn<T, Boolean> deleteCol = new TableColumn<>();

		deleteCol.setCellValueFactory(par -> new SimpleBooleanProperty(par.getValue() != null));
		deleteCol.setCellFactory(param -> {
			Button button = new Button();
			ButtonCell<T> buttonCell = new ButtonCell<>(button);
			Observables.fromNodeEvents(button, ActionEvent.ACTION)
					.map(event -> buttonCell.getTableView().getItems().get(buttonCell.getIndex()))
					.doOnNext(this.data::remove)
					.subscribe();
			return buttonCell;
		});

		return deleteCol;
	}

	protected VBox initNavigationPane() {
		VBox nav = new VBox(15, this.up, this.down, this.add);
		nav.setId("nav-pane");
		nav.setAlignment(Pos.CENTER);

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

	public Observable<List<? extends T>> getData() {
		return Observables.fromObservableList(this.data);
	}

	public void setData(List<T> list) {
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
}
