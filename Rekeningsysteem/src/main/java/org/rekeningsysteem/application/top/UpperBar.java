package org.rekeningsysteem.application.top;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;

public class UpperBar extends ToolBar {

	private final ImageView logo = new ImageView(new Image(Main.getResource("/images/logo.png")));
	private final ImageView name = new ImageView(new Image(Main.getResource("/images/name.png")));

	private final Region leftSpacer = new Region();
	private final Region rightSpacer = new Region();

	private final Button closeBtn = new Button();
	private final Button minBtn = new Button();
	private final Button maxBtn = new Button();

	private final Observable<ActionEvent> closeButtonEvents;
	private final Observable<ActionEvent> minButtonEvents;
	private final Observable<ActionEvent> maxButtonEvents;

	public UpperBar() {
		this.setId("mainToolBar");
		this.getItems().addAll(this.logo, this.leftSpacer, this.name, this.rightSpacer,
				new VBox(4, this.closeBtn, this.minBtn, this.maxBtn));
		this.setPrefHeight(66);
		this.setMinHeight(66);
		this.setMaxHeight(66);

		HBox.setMargin(this.logo, new Insets(0, 0, 0, 5));
		this.name.setId("name-image");

		HBox.setHgrow(this.leftSpacer, Priority.ALWAYS);
		HBox.setHgrow(this.rightSpacer, Priority.ALWAYS);

		this.closeBtn.setId("window-close");
		this.closeButtonEvents = Observables.fromNodeEvents(this.closeBtn, ActionEvent.ACTION);

		this.minBtn.setId("window-min");
		this.minButtonEvents = Observables.fromNodeEvents(this.minBtn, ActionEvent.ACTION);

		this.maxBtn.setId("window-max");
		this.maxButtonEvents = Observables.fromNodeEvents(this.maxBtn, ActionEvent.ACTION);
	}

	public Observable<ActionEvent> getCloseButtonEvents() {
		return this.closeButtonEvents;
	}

	public Observable<ActionEvent> getMinButtonEvents() {
		return this.minButtonEvents;
	}

	public Observable<ActionEvent> getMaxButtonEvents() {
		return this.maxButtonEvents;
	}
}
