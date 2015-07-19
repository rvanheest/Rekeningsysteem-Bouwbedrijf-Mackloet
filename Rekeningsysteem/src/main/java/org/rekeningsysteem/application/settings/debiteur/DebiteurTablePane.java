package org.rekeningsysteem.application.settings.debiteur;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.rxjavafx.Observables;

public class DebiteurTablePane extends VBox {

	public DebiteurTablePane(DebiteurTable table) {
		this.getStyleClass().addAll("working-pane", "page");
		
		Label header = new Label("Debiteur instellingen");
		header.setId("title");
		
		this.getChildren().addAll(header, table);
		
		Observables.fromProperty(this.heightProperty())
        		.map(Number::doubleValue)
        		.map(d -> Math.min(d, 700))
        		.subscribe(table::setPrefHeight);
	}
}
