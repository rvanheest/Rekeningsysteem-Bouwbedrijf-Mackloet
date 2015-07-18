package org.rekeningsysteem.application.settings.debiteur;

import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;

public class DebiteurTablePaneController {

	private DebiteurTablePane ui;

	public DebiteurTablePaneController(Database database) {
		this(new DebiteurTableController(new DebiteurDBInteraction(database)));
	}

	public DebiteurTablePaneController(DebiteurTableController subController) {
		this.ui = new DebiteurTablePane(subController.getUI());
	}

	public DebiteurTablePane getUI() {
		return this.ui;
	}
}
