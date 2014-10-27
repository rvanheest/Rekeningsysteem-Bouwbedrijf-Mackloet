package org.rekeningsysteem.application;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;

public class WindowResizeButton extends Region {

	public WindowResizeButton(Stage stage, double stageMinWidth, double stageMinHeight) {
		this.setId("window-resize-button");
		this.setPrefSize(11, 11);

		Observable<Point2D> pressed = Observables.fromNodeEvents(this, MouseEvent.MOUSE_PRESSED)
				.doOnNext(MouseEvent::consume)
				.map(event -> new Point2D(stage.getX() + stage.getWidth() - event.getScreenX(),
						stage.getY() + stage.getHeight() - event.getScreenY()));

		Observable<Point2D> dragged = Observables.fromNodeEvents(this, MouseEvent.MOUSE_DRAGGED)
				.doOnNext(MouseEvent::consume)
				.map(event -> new Point2D(event.getScreenX(), event.getScreenY()));

		Observable<Rectangle2D> bounds = Observables.fromObservableList(
				Screen.getScreensForRectangle(stage.getX(), stage.getY(), 1, 1))
				.map(s -> !s.isEmpty() ? s.get(0).getVisualBounds()
						: Screen.getScreensForRectangle(0, 0, 1, 1).get(0).getVisualBounds());

		Observable.combineLatest(pressed, dragged, bounds, (press, dragg, bound) -> new Point2D(
				Math.min(bound.getMaxX(), dragg.getX() + press.getX()),
				Math.min(bound.getMaxY(), dragg.getY() + press.getY())))
				.doOnNext(p -> stage.setWidth(Math.max(stageMinWidth, p.getX() - stage.getX())))
				.doOnNext(p -> stage.setHeight(Math.max(stageMinHeight, p.getY() - stage.getY())))
				.subscribe();
	}
}
