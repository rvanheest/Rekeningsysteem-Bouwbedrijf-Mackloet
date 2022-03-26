package org.rekeningsysteem.ui.offerte;

import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.TextArea;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

public class TextPane extends Page {

	private final TextArea textTA = new TextArea();
	private final Observable<String> text = Observables.fromProperty(this.textTA.textProperty());

	public TextPane() {
		super("Offerte tekst");
		
		this.getChildren().add(this.textTA);
		this.textTA.setPrefColumnCount(50);
		this.textTA.setPrefRowCount(20);
		this.textTA.setWrapText(true);
	}

	public Observable<String> getText() {
		return this.text;
	}

	public void setText(String omschrijving) {
		this.textTA.setText(omschrijving);
	}
}
