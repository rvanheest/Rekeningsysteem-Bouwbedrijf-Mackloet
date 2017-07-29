package org.rekeningsysteem.application.settings;

import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import org.apache.log4j.Logger;
import org.rekeningsysteem.application.settings.debiteur.DebiteurTableController;
import org.rekeningsysteem.application.settings.debiteur.DebiteurTablePane;
import org.rekeningsysteem.application.settings.offerte.DefaultOfferteTextPaneController;
import org.rekeningsysteem.application.settings.prijslijst.PrijsLijstController;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logic.database.ArtikellijstDBInteraction;
import org.rekeningsysteem.logic.database.DebiteurDBInteraction;

public class SettingsPane extends TabPane {

	public SettingsPane(Stage stage, ButtonBase closeButton, Database database, Logger logger) {
		this.setId("settings-tabs");
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		DebiteurDBInteraction dbInteraction = new DebiteurDBInteraction(database);
		DebiteurTableController debC = new DebiteurTableController(dbInteraction);
		Tab debiteurTab = new Tab("Debiteur beheer", new DebiteurTablePane(debC.getUI()));

		PrijsLijstController prijslijstC = new PrijsLijstController(stage, closeButton,
				new ArtikellijstDBInteraction(database), logger);
		Tab prijslijstTab = new Tab("Esselink artikel data", prijslijstC.getUI());

		DefaultOfferteTextPaneController offerteC = new DefaultOfferteTextPaneController(logger);
		Tab offerteTab = new Tab("Offerte", offerteC.getUI());

		this.getTabs().addAll(debiteurTab, prijslijstTab, offerteTab);
	}
}
