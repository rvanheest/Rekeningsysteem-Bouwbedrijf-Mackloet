package org.rekeningsysteem.ui.offerte;

import javafx.scene.control.CheckBox;

import org.rekeningsysteem.rxjavafx.Observables;
import org.rekeningsysteem.ui.Page;

import rx.Observable;

public class OndertekenenPane extends Page {

	private final CheckBox checkbox = new CheckBox("ondertekenen");
	private final Observable<Boolean> ondertekenen = Observables
			.fromProperty(this.checkbox.selectedProperty());

	public OndertekenenPane() {
		super("Ondertekenen");

		this.getChildren().add(this.checkbox);
	}

	public Observable<Boolean> isOndertekenen() {
		return this.ondertekenen;
	}

	public void setOndertekenen(boolean ondertekenen) {
		this.checkbox.setSelected(ondertekenen);
	}
}
