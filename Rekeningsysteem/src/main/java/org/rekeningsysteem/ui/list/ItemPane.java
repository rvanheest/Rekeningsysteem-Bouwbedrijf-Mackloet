package org.rekeningsysteem.ui.list;

import io.reactivex.rxjava3.core.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.rxjavafx.Observables;

public abstract class ItemPane extends VBox {

	protected final Label title;

	protected final Button addBtn = new Button("Voeg toe");
	private final Button cancelBtn = new Button("Annuleren");

	public ItemPane(String titleString) {
		this.getStyleClass().add("new-artikel");
		this.setMaxSize(430, USE_PREF_SIZE);

		// block mouse events
		Observables.fromNodeEvents(this, MouseEvent.MOUSE_CLICKED)
			.subscribe(Event::consume);

		this.title = new Label(titleString);
		this.title.setId("title");
		this.title.setMaxWidth(Double.MAX_VALUE);
		this.getChildren().add(this.title);

		this.cancelBtn.setId("cancelButton");
		HBox.setMargin(this.cancelBtn, new Insets(0, 8, 0, 0));

		this.addBtn.setId("addButton");
		this.addBtn.setDefaultButton(true);

		HBox buttonBar = new HBox(0);
		buttonBar.setAlignment(Pos.BASELINE_RIGHT);
		VBox.setMargin(buttonBar, new Insets(20, 5, 5, 5));
		buttonBar.getChildren().addAll(this.cancelBtn, this.addBtn);

		this.getChildren().add(buttonBar);
	}

	public Observable<ActionEvent> getAddButtonEvent() {
		return Observables.fromNodeEvents(this.addBtn, ActionEvent.ACTION);
	}

	public Observable<ActionEvent> getCancelButtonEvent() {
		return Observables.fromNodeEvents(this.cancelBtn, ActionEvent.ACTION);
	}
}
