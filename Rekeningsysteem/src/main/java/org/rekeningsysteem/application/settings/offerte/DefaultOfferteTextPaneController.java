package org.rekeningsysteem.application.settings.offerte;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

import org.apache.log4j.Logger;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;

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

		this.handler.getDefaultText().subscribe(this.ui::setText);

		text.sample(save)
				.compose(this.handler::setDefaultText)
				.observeOn(JavaFxScheduler.getInstance())
				.doOnError(e -> {
					String alertText = "De tekst kon niet worden opgeslagen. "
							+ "Zie de log voor meer info.";
					ButtonType close = new ButtonType("Sluit", ButtonData.CANCEL_CLOSE);
					Alert alert = new Alert(AlertType.NONE, alertText, close);
					alert.setHeaderText("Fout bij opslaan");
					alert.show();
					this.logger.error(e.getMessage(), e);
				})
				.subscribe();

		cancel.flatMap(e -> this.handler.getDefaultText())
				.subscribe(this.ui::setText);
	}

	public DefaultOfferteTextPane getUI() {
		return this.ui;
	}
}
