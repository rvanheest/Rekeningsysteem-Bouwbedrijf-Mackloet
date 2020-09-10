package org.rekeningsysteem.application;

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

import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;

public class UpperBar extends ToolBar {

	private final Observable<ActionEvent> closeButtonEvents;
	private final Observable<ActionEvent> minButtonEvents;
	private final Observable<ActionEvent> maxButtonEvents;

	public UpperBar() {
		Region leftSpacer = new Region();
		Region rightSpacer = new Region();

		Button closeBtn = new Button();
		Button minBtn = new Button();
		Button maxBtn = new Button();

		this.setId("mainToolBar");

		PropertiesWorker properties = PropertiesWorker.getInstance();
		properties.getProperty(PropertyModelEnum.APPLICATION_LOGO)
			.map(path -> new ImageView(new Image(Main.getExternalResource(path))))
			.ifPresent(img -> {
				this.getItems().add(img);
				HBox.setMargin(img, new Insets(0, 0, 0, 5));
			});
		this.getItems().add(leftSpacer);
		properties.getProperty(PropertyModelEnum.APPLICATION_NAME_LOGO)
			.map(path -> new ImageView(new Image(Main.getExternalResource(path))))
			.ifPresent(this.getItems()::add);
		this.getItems().addAll(rightSpacer, new VBox(4, closeBtn, minBtn, maxBtn));

		this.setPrefHeight(66);
		this.setMinHeight(66);
		this.setMaxHeight(66);

		HBox.setHgrow(leftSpacer, Priority.ALWAYS);
		HBox.setHgrow(rightSpacer, Priority.ALWAYS);

		closeBtn.setId("window-close");
		this.closeButtonEvents = Observables.fromNodeEvents(closeBtn, ActionEvent.ACTION);

		minBtn.setId("window-min");
		this.minButtonEvents = Observables.fromNodeEvents(minBtn, ActionEvent.ACTION);

		maxBtn.setId("window-max");
		this.maxButtonEvents = Observables.fromNodeEvents(maxBtn, ActionEvent.ACTION);
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
