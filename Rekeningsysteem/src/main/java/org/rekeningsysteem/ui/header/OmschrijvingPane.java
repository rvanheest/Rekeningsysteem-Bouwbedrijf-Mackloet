package org.rekeningsysteem.ui.header;

import io.reactivex.rxjava3.core.Observable;
import javafx.scene.control.TextArea;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

public class OmschrijvingPane extends Page {

	private final TextArea omschrTA = new TextArea();
	private final Observable<String> omschrijving = Observables.fromProperty(this.omschrTA.textProperty());

	public OmschrijvingPane() {
		super("Omschrijving");

		this.omschrTA.setPrefColumnCount(50);
		this.omschrTA.setPrefRowCount(5);

		this.getChildren().add(this.omschrTA);
	}

	public Observable<String> getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrTA.setText(omschrijving);
	}
}
