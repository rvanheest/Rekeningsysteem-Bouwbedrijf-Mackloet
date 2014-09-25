package org.rekeningsysteem.application.working;

import org.rekeningsysteem.application.guice.TabName;

import com.google.inject.Inject;

import javafx.scene.control.Tab;

public class RekeningTab extends Tab {

	private final RekeningSplitPane splitPane;

	@Inject
	public RekeningTab(@TabName String name, RekeningSplitPane splitPane) {
		super(name);
		this.splitPane = splitPane;

		this.setContent(this.splitPane);
	}
}
