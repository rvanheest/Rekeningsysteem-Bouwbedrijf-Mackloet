package org.rekeningsysteem.application.top;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import org.rekeningsysteem.application.Main;
import org.rekeningsysteem.rxjavafx.Observables;

import com.google.inject.Inject;

import rx.Observable;

public class UpperBar extends ToolBar {

	@Inject
	public UpperBar(Stage stage, WindowButtons windowButtons) {
		this.setId("mainToolBar");

		this.setPrefHeight(66);
		this.setMinHeight(66);
		this.setMaxHeight(66);

		ImageView logo = new ImageView(new Image(Main.getResource("/images/logo.png")));
		HBox.setMargin(logo, new Insets(0, 0, 0, 5));
		this.getItems().add(logo);

		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		this.getItems().add(spacer);

		ImageView name = new ImageView(new Image(Main.getResource("/images/name.png")));
		name.setId("name-image");
		this.getItems().add(name);

		Region spacer2 = new Region();
		HBox.setHgrow(spacer2, Priority.ALWAYS);
		this.getItems().add(spacer2);

		this.getItems().add(windowButtons);

		Observables.fromNodeEvents(this, MouseEvent.MOUSE_CLICKED)
				.filter(event -> event.getClickCount() == 2)
				.doOnNext(MouseEvent::consume)
				.subscribe(event -> windowButtons.toggleMaximized());

		Observable<Point2D> pressed = Observables.fromNodeEvents(this, MouseEvent.MOUSE_PRESSED)
				.doOnNext(MouseEvent::consume)
				.filter(event -> !windowButtons.isMaximized())
				.filter(event -> event.getClickCount() == 1)
				.map(event -> new Point2D(event.getSceneX(), event.getSceneY()));
		Observable<Point2D> dragged = Observables.fromNodeEvents(this, MouseEvent.MOUSE_DRAGGED)
				.doOnNext(MouseEvent::consume)
				.filter(event -> !windowButtons.isMaximized())
				.filter(event -> event.getClickCount() == 1)
				.map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
		Observable.combineLatest(dragged, pressed.sample(dragged), Point2D::subtract)
				.doOnNext(point -> stage.setX(point.getX()))
				.doOnNext(point -> stage.setY(point.getY()))
				.subscribe();
	}
}
