package org.rekeningsysteem.ui;

import javafx.scene.layout.VBox;

public abstract class WorkingPane extends VBox {

	protected WorkingPane(Page... nodes) {
		super(nodes);
		this.getStyleClass().add("working-pane");
	}

	public abstract String getTitle();
}
