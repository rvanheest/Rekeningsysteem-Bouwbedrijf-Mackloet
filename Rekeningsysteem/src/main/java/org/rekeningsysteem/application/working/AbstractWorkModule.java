package org.rekeningsysteem.application.working;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.functions.Func1;

public abstract class AbstractWorkModule {

	private final Button button = new Button();
	private final Func1<ActionEvent, RekeningTab> tab;

	protected AbstractWorkModule(ImageView buttonImage, Func1<ActionEvent, RekeningTab> tab) {
		this.button.setGraphic(buttonImage);
		this.tab = tab;
	}

	public Button getButton() {
		return this.button;
	}

	public Observable<RekeningTab> getButtonEvent() {
		return Observables.fromNodeEvents(this.button, ActionEvent.ACTION)
				.map(this.tab);
	}
}
