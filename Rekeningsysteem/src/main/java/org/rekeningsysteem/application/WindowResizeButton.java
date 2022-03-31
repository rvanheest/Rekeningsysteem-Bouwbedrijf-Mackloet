package org.rekeningsysteem.application;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.rekeningsysteem.rxjavafx.Observables;

import java.util.List;

public class WindowResizeButton extends Region implements Disposable {

	private final CompositeDisposable disposable = new CompositeDisposable();

	public WindowResizeButton(Stage stage, double stageMinWidth, double stageMinHeight) {
		this.setId("window-resize-button");
		this.setPrefSize(11, 11);

		this.disposable.add(
			Observable.combineLatest(
					Observables.fromNodeEvents(this, MouseEvent.MOUSE_PRESSED).doOnNext(MouseEvent::consume).map(event -> getPressedPoint(stage, event)),
					Observables.fromNodeEvents(this, MouseEvent.MOUSE_DRAGGED).doOnNext(MouseEvent::consume).map(this::getDraggPoint),
					Observables.fromObservableList(Screen.getScreensForRectangle(stage.getX(), stage.getY(), 1, 1)).map(this::getBoundRectangle),
					this::combinePoints
				)
				.subscribe(point -> setStageDimensions(stage, stageMinWidth, stageMinHeight, point))
		);
	}

	private Point2D getPressedPoint(Stage stage, MouseEvent event) {
		return new Point2D(stage.getX() + stage.getWidth() - event.getScreenX(), stage.getY() + stage.getHeight() - event.getScreenY());
	}

	private Point2D getDraggPoint(MouseEvent event1) {
		return new Point2D(event1.getScreenX(), event1.getScreenY());
	}

	private Rectangle2D getBoundRectangle(List<? extends Screen> s) {
		return !s.isEmpty() ? s.get(0).getVisualBounds() : Screen.getScreensForRectangle(0, 0, 1, 1).get(0).getVisualBounds();
	}

	private Point2D combinePoints(Point2D press, Point2D dragg, Rectangle2D bound) {
		return new Point2D(Math.min(bound.getMaxX(), dragg.getX() + press.getX()), Math.min(bound.getMaxY(), dragg.getY() + press.getY()));
	}

	private void setStageDimensions(Stage stage, double stageMinWidth, double stageMinHeight, Point2D point) {
		stage.setWidth(Math.max(stageMinWidth, point.getX() - stage.getX()));
		stage.setHeight(Math.max(stageMinHeight, point.getY() - stage.getY()));
	}

	@Override
	public boolean isDisposed() {
		return this.disposable.isDisposed();
	}

	@Override
	public void dispose() {
		this.disposable.dispose();
	}
}
