package org.rekeningsysteem.application.settings;

import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.rekeningsysteem.application.settings.debiteur.DebiteurTablePaneController;
import org.rekeningsysteem.application.settings.offerte.DefaultOfferteTextPaneController;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;

public class SettingsPane extends TabPane {

	public SettingsPane(Stage stage, ButtonBase closeButton, Database database, Logger logger) {
		this.setId("settings-tabs");
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		DebiteurTablePaneController debiteurC = new DebiteurTablePaneController(database);
		Tab debiteurTab = new Tab("Debiteur beheer", debiteurC.getUI());

		PrijslijstIO prijslijstTab = new PrijslijstIO(stage,
				new ArtikellijstDBInteraction(database), closeButton, logger);

		DefaultOfferteTextPaneController offerteC = new DefaultOfferteTextPaneController(logger);
		Tab offerteTab = new Tab("Offerte", offerteC.getUI());

		this.getTabs().addAll(debiteurTab, prijslijstTab, offerteTab);
	}
}
