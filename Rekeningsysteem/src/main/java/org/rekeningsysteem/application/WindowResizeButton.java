package org.rekeningsysteem.application;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.functions.Func3;

public class WindowResizeButton extends Region {

	public WindowResizeButton(Stage stage, double stageMinimumWidth, double stageMinimumHeight) {
		this.setId("window-resize-button");
		this.setPrefSize(11, 11);
		
		Func3<Point2D, Point2D, Rectangle2D, Point2D> func = (pressed, dragged, bounds) -> new Point2D(
				Math.min(bounds.getMaxX(), dragged.getX() + pressed.getX()),
				Math.min(bounds.getMaxY(), dragged.getY() + pressed.getY()));
		
		Observable<Point2D> pressed = Observables.fromNodeEvents(this, MouseEvent.MOUSE_PRESSED)
				.doOnNext(MouseEvent::consume)
				.map(event -> new Point2D(stage.getX() + stage.getWidth() - event.getScreenX(),
						stage.getY() + stage.getHeight() - event.getScreenY()));
		
		Observable<Point2D> dragged = Observables.fromNodeEvents(this, MouseEvent.MOUSE_DRAGGED)
			.doOnNext(MouseEvent::consume)
			.map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
		
		Observable<Rectangle2D> bounds = Observables.fromObservableList(Screen.getScreensForRectangle(stage.getX(), stage.getY(), 1, 1))
				.map(s -> !s.isEmpty() ? s.get(0).getVisualBounds()
						: Screen.getScreensForRectangle(0, 0, 1, 1).get(0).getVisualBounds());

		Observable.combineLatest(pressed, dragged, bounds, func)
				.doOnNext(p -> stage.setWidth(Math.max(stageMinimumWidth,
						p.getX() - stage.getX())))
				.doOnNext(p -> stage.setHeight(Math.max(stageMinimumHeight,
						p.getY() - stage.getY())))
				.subscribe();
	}
}
