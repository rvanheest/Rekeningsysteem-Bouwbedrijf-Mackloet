package org.rekeningsysteem.application.working;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import org.rekeningsysteem.application.guice.ButtonImage;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.functions.Func1;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

public class WorkUnit {

	private final Button button = new Button();
	private final Func1<ActionEvent, RekeningTab> tab;

	public WorkUnit(AbstractModule... modules) {
		Injector injector = Guice.createInjector(modules);
		this.button.setGraphic(injector.getInstance(Key.get(ImageView.class, ButtonImage.class)));
		this.tab = event -> injector.getInstance(RekeningTab.class);
	}

	public Button getButton() {
		return this.button;
	}

	public Observable<RekeningTab> getNewRekeningTab() {
		return Observables.fromNodeEvents(this.button, ActionEvent.ACTION)
				.map(this.tab);
	}
}
