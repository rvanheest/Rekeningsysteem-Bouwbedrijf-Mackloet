package org.rekeningsysteem.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public abstract class Page extends VBox {

	protected Page(String headerText) {
		this.getStyleClass().add("page");
		
		Label header = new Label(headerText);
		header.setId("title");
		
		this.getChildren().add(header);
	}
}
