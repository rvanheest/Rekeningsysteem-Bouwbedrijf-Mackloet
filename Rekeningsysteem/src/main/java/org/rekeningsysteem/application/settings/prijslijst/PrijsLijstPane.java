package org.rekeningsysteem.application.settings.prijslijst;

import io.reactivex.rxjava3.core.Observable;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import org.rekeningsysteem.rxjavafx.Observables;

public class PrijsLijstPane extends GridPane {

	private final Label progressLabel = new Label("nog niet gestart");
	private final Button startButton = new Button("Start");

	public PrijsLijstPane() {
		this.progressLabel.getStyleClass().add("no-item-found");
		Label warningLabel = new Label("Let op, de huidige data wordt verwijderd wanneer nieuwe data wordt geïmporteerd!");
		warningLabel.setTextFill(Color.RED);
		warningLabel.setWrapText(true);

		this.getStyleClass().addAll("working-pane", "page");
		this.setPadding(new Insets(8));
		this.setHgap(10);
		this.setVgap(5);

		this.add(warningLabel, 0, 0, 2, 1);
		this.add(this.startButton, 0, 1);
		this.add(this.progressLabel, 1, 1);
	}

	public Observable<ActionEvent> getStartButtonEvents() {
		return Observables.fromNodeEvents(this.startButton, ActionEvent.ACTION);
	}

	public void setStartButtonDisable(boolean value) {
		this.startButton.setDisable(value);
	}

	public void setProgressText(String value) {
		this.progressLabel.setText(value);
	}

	public String getProgressText() {
		return this.progressLabel.getText();
	}
}
