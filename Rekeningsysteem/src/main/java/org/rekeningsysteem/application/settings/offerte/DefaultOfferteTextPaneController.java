package org.rekeningsysteem.application.settings.offerte;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

import org.apache.log4j.Logger;
import org.rekeningsysteem.exception.NoSuchFileException;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;

import rx.Observable;

public class DefaultOfferteTextPaneController {

	private final DefaultOfferteTextPane ui;
	private final DefaultOfferteTextHandler handler = new DefaultOfferteTextHandler();
	
	private final Logger logger = ApplicationLogger.getInstance();

	public DefaultOfferteTextPaneController() {
		this.ui = new DefaultOfferteTextPane();

		Observable<String> text = this.ui.getText();
		Observable<ActionEvent> save = this.ui.getSaveButtonEvent();
		Observable<ActionEvent> cancel = this.ui.getCancelButtonEvent();

		this.ui.setText(this.handler.getDefaultText());

		text.sample(save)
				.subscribe(s -> {
					try {
						this.handler.setDefaultText(s);
					}
					catch (IOException e) {
						this.logger.error("Error while writing default offerte text to file.", e);
					}
					catch (NoSuchFileException e) {
						String alertText = "De tekst kon niet worden opgeslagen. Raadpleeg "
								+ "de programmeur om dit probleem op te lossen.";
						ButtonType close = new ButtonType("Sluit", ButtonData.CANCEL_CLOSE);
						Alert alert = new Alert(AlertType.NONE, alertText, close);
						alert.setHeaderText("Fout bij opslaan");
						alert.show();
						this.logger.error(e.getMessage() + "\n" + "De tekst was: \"" + s + "\"\n", e);
					}
				});
		cancel.subscribe(e -> {
			String s = this.handler.getDefaultText();
			this.ui.setText(s);
		});
	}

	public DefaultOfferteTextPane getUI() {
		return this.ui;
	}
}
