package org.rekeningsysteem.application.settings;

import org.rekeningsysteem.application.settings.debiteur.DebiteurSettingsTabController;
import org.rekeningsysteem.application.settings.offerte.DefaultOfferteTextTabController;
import org.rekeningsysteem.io.database.Database;

import javafx.scene.control.ButtonBase;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class SettingsPane extends TabPane {

	public SettingsPane(Stage stage, ButtonBase closeButton, Database database) {
		this.setId("settings-tabs");
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		PrijslijstIO prijslijstTab = new PrijslijstIO(stage, closeButton);
		DebiteurSettingsTabController debiteurTab = new DebiteurSettingsTabController(database);
		DefaultOfferteTextTabController offerteTab = new DefaultOfferteTextTabController();
		
		this.getTabs().addAll(prijslijstTab, debiteurTab.getUI(), offerteTab.getUI());
	}
}
