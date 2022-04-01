package org.rekeningsysteem.application.settings.offerte;

import io.reactivex.rxjava3.core.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.rxjavafx.Observables;

public class DefaultOfferteTextPane extends VBox {

	private final TextArea textTA = new TextArea();
	private final Button saveButton = new Button("Opslaan");
	private final Button cancelButton = new Button("Cancel");

	private final Observable<String> text = Observables.fromProperty(this.textTA.textProperty());
	private final Observable<ActionEvent> save = Observables.fromNodeEvents(this.saveButton, ActionEvent.ACTION);
	private final Observable<ActionEvent> cancel = Observables.fromNodeEvents(this.cancelButton, ActionEvent.ACTION);

	public DefaultOfferteTextPane() {
		this.getStyleClass().addAll("working-pane", "page");
		this.setAlignment(Pos.TOP_CENTER);
		this.setPadding(new Insets(15));
		this.setSpacing(10);

		Label header = new Label("Standaard offerte tekst");
		header.setId("title");
		
		this.textTA.setPrefColumnCount(50);
		this.textTA.setPrefRowCount(20);
		this.textTA.setWrapText(true);
		
		this.cancelButton.setId("cancelButton");
		this.cancelButton.setMinWidth(74);
		this.cancelButton.setPrefWidth(74);
		HBox.setMargin(this.cancelButton, new Insets(0, 8, 0, 0));
		
		this.saveButton.setId("addButton");
		this.saveButton.setMinWidth(74);
		this.saveButton.setPrefWidth(74);
		this.saveButton.setDefaultButton(true);
		
		HBox buttons = new HBox(0, this.cancelButton, this.saveButton);
		buttons.setAlignment(Pos.BASELINE_RIGHT);
		VBox.setMargin(buttons, new Insets(10, 5, 5, 5));
		
		VBox content = new VBox(this.textTA, buttons);
		
		this.getChildren().addAll(header, content);
	}

	public Observable<String> getText() {
		return this.text;
	}

	public void setText(String text) {
		this.textTA.setText(text);
	}

	public Observable<ActionEvent> getSaveButtonEvent() {
		return this.save;
	}

	public Observable<ActionEvent> getCancelButtonEvent() {
		return this.cancel;
	}
}
