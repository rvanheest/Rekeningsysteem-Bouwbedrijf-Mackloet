package org.rekeningsysteem.ui;

import javafx.scene.layout.VBox;

public class WorkingPane extends VBox {

	private final String title;

	public WorkingPane(String title, Page... nodes) {
		super(nodes);
		this.getStyleClass().add("working-pane");
		this.title = title;
	}

	public final String getTitle() {
		return this.title;
	}
}
