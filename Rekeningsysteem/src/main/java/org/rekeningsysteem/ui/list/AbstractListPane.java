package org.rekeningsysteem.ui.list;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
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

public abstract class AbstractListPane<T> extends Page implements Disposable {

	private final TableView<T> table = new TableView<>();
	private final ObservableList<T> data = FXCollections.observableArrayList();
	private final CompositeDisposable disposable = new CompositeDisposable();

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

		this.disposable.add(
			Observables.fromProperty(this.heightProperty()).map(Number::doubleValue).subscribe(this.table::setPrefHeight)
		);
	}

	protected abstract List<TableColumn<T, ?>> initTableColumns();

	protected TableColumn<T, Boolean> getDeleteCol() {
		TableColumn<T, Boolean> deleteCol = new TableColumn<>();

		deleteCol.setCellValueFactory(par -> new SimpleBooleanProperty(par.getValue() != null));
		deleteCol.setCellFactory(param -> {
			Button button = new Button();
			ButtonCell<T> buttonCell = new ButtonCell<>(button);
			this.disposable.add(
				Observables.fromNodeEvents(button, ActionEvent.ACTION)
					.map(event -> buttonCell.getTableView().getItems().get(buttonCell.getIndex()))
					.subscribe(this.data::remove)
			);
			return buttonCell;
		});

		return deleteCol;
	}

	protected VBox initNavigationPane() {
		VBox nav = new VBox(15, this.up, this.down, this.add);
		nav.setId("nav-pane");
		nav.setAlignment(Pos.CENTER);

		this.disposable.add(
			Observables.fromProperty(this.table.getSelectionModel().selectedIndexProperty())
				.map(Number::intValue)
				.subscribe(i -> {
					int size = this.data.size();
					int max = Math.max(0, size - 1);
					if (size > 1 && i == 0) {
						this.setUpDownDisabled(true, false);
					}
					else if (size > 1 && i == max) {
						this.setUpDownDisabled(false, true);
					}
					else if (size > 0 && i > 0 && i < max) {
						this.setUpDownDisabled(false, false);
					}
					else if (i == -1 || size == 0 || size == 1) {
						this.setUpDownDisabled(true, true);
					}
				})
		);

		return nav;
	}

	private void setUpDownDisabled(boolean upDisabled, boolean downDisabled) {
		this.up.setDisable(upDisabled);
		this.down.setDisable(downDisabled);
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

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}
