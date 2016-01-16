package org.rekeningsysteem.application.settings;

import org.apache.log4j.Logger;
import org.rekeningsysteem.application.settings.debiteur.DebiteurSettingsTabController;
import org.rekeningsysteem.application.settings.offerte.DefaultOfferteTextTabController;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;

import javafx.scene.control.ButtonBase;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class SettingsPane extends TabPane {

	public SettingsPane(Stage stage, ButtonBase closeButton, Database database, Logger logger) {
		this.setId("settings-tabs");
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		DebiteurSettingsTabController debiteurTab = new DebiteurSettingsTabController(database);
		PrijslijstIO prijslijstTab = new PrijslijstIO(stage,
				new ArtikellijstDBInteraction(database), closeButton, logger);
		DefaultOfferteTextTabController offerteTab = new DefaultOfferteTextTabController(logger);

		this.getTabs().addAll(debiteurTab.getUI(), prijslijstTab, offerteTab.getUI());
	}
}
