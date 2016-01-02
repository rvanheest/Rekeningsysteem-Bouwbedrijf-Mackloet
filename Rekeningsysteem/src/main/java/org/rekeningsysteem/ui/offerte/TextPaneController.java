package org.rekeningsysteem.ui.offerte;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;

import org.apache.log4j.Logger;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;
import org.rekeningsysteem.rxjavafx.JavaFxScheduler;
import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.WorkingPaneController;

import rx.Observable;

public class TextPaneController extends WorkingPaneController {

	private final TextPane ui;
	private final Observable<String> model;

	public TextPaneController(Logger logger) {
		this(new TextPane(), logger);
	}

	public TextPaneController(String input, Logger logger) {
		this(logger);
		this.ui.setText(input);
	}

	public TextPaneController(TextPane ui, Logger logger) {
		super(new WorkingPane(ui) {

			@Override
			public String getTitle() {
				return "Offerte tekst";
			}
		});

		this.ui = ui;
		this.model = this.ui.getText();

		this.initDefaultText(logger);
	}

	private void initDefaultText(Logger logger) {
		new DefaultOfferteTextHandler()
				.getDefaultText()
				.observeOn(JavaFxScheduler.getInstance())
				.subscribe(
						this.ui::setText,
						e -> {
							String alertText = "De standaard tekst voor de offerte kon niet worden geladen. Zie de error log voor meer info.";
							ButtonType close = new ButtonType("Sluit", ButtonData.CANCEL_CLOSE);
							Alert alert = new Alert(AlertType.ERROR, alertText, close);
							alert.setHeaderText("Fout bij lezen");
							alert.show();
							logger.error("error in reading default offerte text file", e);
						});
	}

	public Observable<String> getModel() {
		return this.model;
	}
}
