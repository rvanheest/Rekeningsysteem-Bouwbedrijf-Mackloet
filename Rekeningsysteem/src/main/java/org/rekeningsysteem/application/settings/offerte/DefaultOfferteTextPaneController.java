package org.rekeningsysteem.application.settings.offerte;

import java.io.IOException;

import javafx.event.ActionEvent;

import org.rekeningsysteem.exception.NoSuchFileException;
import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;

import rx.Observable;

public class DefaultOfferteTextPaneController {

	private final DefaultOfferteTextPane ui;
	private final DefaultOfferteTextHandler handler = new DefaultOfferteTextHandler();

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
						e.printStackTrace();
					}
					catch (NoSuchFileException e) {
						e.printStackTrace();
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
