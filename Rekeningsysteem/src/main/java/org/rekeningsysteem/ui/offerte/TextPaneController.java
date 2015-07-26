package org.rekeningsysteem.ui.offerte;

import org.rekeningsysteem.logic.offerte.DefaultOfferteTextHandler;
import org.rekeningsysteem.ui.WorkingPane;
import org.rekeningsysteem.ui.WorkingPaneController;

import rx.Observable;

public class TextPaneController extends WorkingPaneController {

	private final TextPane ui;
	private final Observable<String> model;

	public TextPaneController() {
		this(new TextPane());
	}

	public TextPaneController(String input) {
		this();
		this.ui.setText(input);
	}

	public TextPaneController(TextPane ui) {
		super(new WorkingPane(ui) {

			@Override
			public String getTitle() {
				return "Offerte tekst";
			}
		});

		this.ui = ui;
		this.model = this.ui.getText();

		DefaultOfferteTextHandler textHandler = new DefaultOfferteTextHandler();
		String text = textHandler.getDefaultText();
		if (!text.startsWith("\n\n")) {
			text = "\n\n" + text;
		}
		this.ui.setText(text);
	}

	public Observable<String> getModel() {
		return this.model;
	}
}
