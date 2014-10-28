package org.rekeningsysteem.ui.offerte;

import javafx.scene.control.TextArea;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

import rx.Observable;

public class TextPane extends Page {

	private TextArea textTA = new TextArea();
	private Observable<String> text = Observables.fromProperty(this.textTA.textProperty());

	public TextPane() {
		super("Offerte tekst");
		
		this.getChildren().add(this.textTA);
		this.textTA.setPrefColumnCount(50);
	}

	public Observable<String> getText() {
		return this.text;
	}

	public void setText(String omschrijving) {
		this.textTA.setText(omschrijving);
	}
}
