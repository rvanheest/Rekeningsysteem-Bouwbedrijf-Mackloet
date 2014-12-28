package org.rekeningsysteem.application.settings;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.rxjavafx.Observables;

public class SettingsPane extends VBox {

	private final Label title = new Label("Instellingen");
	private final TabPane tabPane = new TabPane();
	private final Button closeBtn = new Button("Sluiten");

	public SettingsPane(Stage stage) {
		this.getStyleClass().add("new-artikel");
		this.setMaxSize(430, USE_PREF_SIZE);

		// block mouse events
		Observables.fromNodeEvents(this, MouseEvent.MOUSE_CLICKED)
				.subscribe(Event::consume);

		this.title.setId("title");
		this.title.setMaxWidth(Double.MAX_VALUE);

		this.createTabPane(stage);

		this.closeBtn.setId("cancelButton");
		HBox.setMargin(this.closeBtn, new Insets(0, 8, 0, 0));
		Observables.fromNodeEvents(this.closeBtn, ActionEvent.ACTION)
				.subscribe(event -> Main.getMain().hideModalMessage());

		HBox buttonBar = new HBox(0);
		buttonBar.setAlignment(Pos.BASELINE_RIGHT);
		VBox.setMargin(buttonBar, new Insets(20, 5, 5, 5));
		buttonBar.getChildren().add(this.closeBtn);

		this.getChildren().addAll(this.title, this.tabPane, buttonBar);
	}

	private void createTabPane(Stage stage) {
		this.tabPane.setId("particulier-tabs");
		this.tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		Tab esselinkTab = new PrijslijstIO(stage, this.closeBtn);

		this.tabPane.getTabs().addAll(esselinkTab);
	}
}
