package org.rekeningsysteem.application.settings.debiteur;

import org.rekeningsysteem.io.database.Database;

public class DebiteurSettingsTabController {

	private final DebiteurSettingsTab ui;

	public DebiteurSettingsTabController(Database database) {
		this(new DebiteurTablePaneController(database));
	}

	public DebiteurSettingsTabController(DebiteurTablePaneController subController) {
		this.ui = new DebiteurSettingsTab(subController.getUI());
	}

	public DebiteurSettingsTab getUI() {
		return this.ui;
	}
}
