package org.rekeningsysteem.application.settings.debiteur;

import javafx.scene.control.Tab;

import org.rekeningsysteem.io.database.Database;

@Deprecated
public class DebiteurSettingsTabController {

	private final Tab ui;

	public DebiteurSettingsTabController(Database database) {
		this(new DebiteurTablePaneController(database));
	}

	public DebiteurSettingsTabController(DebiteurTablePaneController subController) {
		this.ui = new Tab("Debiteur behaar", subController.getUI());
	}

	public Tab getUI() {
		return this.ui;
	}
}
