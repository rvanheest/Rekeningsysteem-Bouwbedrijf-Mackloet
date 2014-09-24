package org.rekeningsysteem.application.working;

import javafx.scene.control.Tab;

public class RekeningTab extends Tab {

	private final RekeningSplitPane splitPane;

	public RekeningTab(String name, RekeningSplitPane splitPane) {
		super(name);
		this.splitPane = splitPane;
		
		this.setContent(this.splitPane);
	}
}
