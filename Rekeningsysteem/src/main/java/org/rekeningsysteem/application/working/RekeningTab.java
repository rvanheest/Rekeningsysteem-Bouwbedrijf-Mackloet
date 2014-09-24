package org.rekeningsysteem.application.working;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;

public class RekeningTab extends Tab {

	public RekeningTab(String name) {
		super(name);
		this.setContent(new SplitPane());
	}
}
