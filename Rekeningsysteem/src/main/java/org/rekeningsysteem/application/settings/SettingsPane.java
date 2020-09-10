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
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyKey;
import org.rekeningsysteem.properties.PropertyModelEnum;

import java.util.Optional;

public class SettingsPane extends TabPane {

	public SettingsPane(Stage stage, ButtonBase closeButton, Database database, Logger logger) {
		this.setId("settings-tabs");
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

		PropertiesWorker properties = PropertiesWorker.getInstance();

		DebiteurDBInteraction dbInteraction = new DebiteurDBInteraction(database);
		DebiteurTableController debC = new DebiteurTableController(dbInteraction);
		Tab debiteurTab = new Tab("Debiteur beheer", new DebiteurTablePane(debC.getUI()));
		this.getTabs().add(debiteurTab);

		properties.getProperty(PropertyModelEnum.FEATURE_PARTICULIER_ESSELINK_ARTIKEL).map(Boolean::parseBoolean)
			.filter(b -> b)
			.ifPresent(b -> {
				PrijsLijstController prijslijstC = new PrijsLijstController(stage, closeButton,
					new ArtikellijstDBInteraction(database), logger);
				Tab prijslijstTab = new Tab("Esselink artikel data", prijslijstC.getUI());
				this.getTabs().add(prijslijstTab);
			});

		properties.getProperty(PropertyModelEnum.FEATURE_OFFERTE).map(Boolean::parseBoolean)
			.filter(b -> b)
			.ifPresent(b -> {
				DefaultOfferteTextPaneController offerteC = new DefaultOfferteTextPaneController(logger);
				Tab offerteTab = new Tab("Offerte", offerteC.getUI());
				this.getTabs().add(offerteTab);
			});
	}
}
