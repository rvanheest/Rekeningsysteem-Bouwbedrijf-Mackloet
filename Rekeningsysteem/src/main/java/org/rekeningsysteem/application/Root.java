package org.rekeningsysteem.application;

import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.application.working.MainPane;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.rxjavafx.Observables;

public class Root extends BorderPane implements Disposable {

	private final WindowResizeButton resizeButton;

	private final PublishSubject<Rectangle2D> windowBounds = PublishSubject.create();
	private final BehaviorSubject<Boolean> max;

	private final CompositeDisposable disposable = new CompositeDisposable();

	public Root(Stage stage, Database database, Logger logger) {
		this.setId("root");

		Boolean fullscreen = PropertiesWorker.getInstance()
			.getProperty(PropertyModelEnum.APPLICATION_FULL_SCREEN_MODE)
			.map(Boolean::parseBoolean)
			.orElse(false);
		this.max = BehaviorSubject.createDefault(fullscreen);

		UpperBar upperBar = new UpperBar();
		this.setTop(upperBar);
		this.setCenter(new MainPane(stage, database, logger));

		this.resizeButton = new WindowResizeButton(stage, Main.screenWidth, Main.screenHeight);
		this.resizeButton.setManaged(false);

		Consumer<WindowResizeButton> add = this.getChildren()::add;
		Consumer<WindowResizeButton> remove = this.getChildren()::remove;

		Observable<Boolean> maxToggle = this.max.scan(!fullscreen, (cum, b) -> !cum)
			.skip(1)
			.doOnNext(b -> PropertiesWorker.getInstance().setProperty(PropertyModelEnum.APPLICATION_FULL_SCREEN_MODE, Boolean.toString(b)));

		this.disposable.addAll(
			this.resizeButton,

			upperBar.getCloseButtonEvents()
				.subscribe(e -> Platform.exit()),

			upperBar.getMinButtonEvents()
				.subscribe(e -> stage.setIconified(true)),

			upperBar.getMaxButtonEvents()
				.map(e -> false)
				.subscribe(this.max::onNext, this.max::onError, this.max::onComplete),

			maxToggle.filter(b -> b)
				.subscribe(b -> Platform.runLater(() -> {
					double x = Math.max(0, stage.getX());
					double y = Math.max(0, stage.getY());
					double width = stage.getWidth();
					double height = stage.getHeight();
					this.windowBounds.onNext(new Rectangle2D(x, y, width, height));

					Rectangle2D screen = Screen.getScreensForRectangle(x, y, 1, 1).get(0).getVisualBounds();
					stage.setX(screen.getMinX());
					stage.setY(screen.getMinY());
					stage.setWidth(screen.getWidth());
					stage.setHeight(screen.getHeight());
				})),

			maxToggle.filter(b -> !b)
				.withLatestFrom(this.windowBounds, (b, windowBounds) -> windowBounds)
				.subscribe(bounds -> {
					stage.setX(bounds.getMinX());
					stage.setY(bounds.getMinY());
					stage.setWidth(bounds.getWidth());
					stage.setHeight(bounds.getHeight());
				}),

			maxToggle.map(b -> b ? remove : add)
				.subscribe(action -> action.accept(this.resizeButton)),

			Observables.fromNodeEvents(upperBar, MouseEvent.MOUSE_CLICKED)
				.filter(event -> event.getClickCount() == 2)
				.doOnNext(MouseEvent::consume)
				.subscribe(event -> this.max.onNext(false))
		);

		Observable<Point2D> pressed = Observables
			.fromNodeEvents(upperBar, MouseEvent.MOUSE_PRESSED)
			.doOnNext(MouseEvent::consume)
			.withLatestFrom(maxToggle, (event, maximized) -> maximized ? null : event)
			.filter(Objects::nonNull)
			.filter(event -> event.getClickCount() == 1)
			.map(event -> new Point2D(event.getSceneX(), event.getSceneY()));

		Observable<Point2D> dragged = Observables
			.fromNodeEvents(upperBar, MouseEvent.MOUSE_DRAGGED)
			.doOnNext(MouseEvent::consume)
			.withLatestFrom(maxToggle, (event, maximized) -> maximized ? null : event)
			.filter(Objects::nonNull)
			.filter(event -> event.getClickCount() == 1)
			.map(event -> new Point2D(event.getScreenX(), event.getScreenY()));

		this.disposable.add(
			Observable.combineLatest(dragged, pressed.sample(dragged), Point2D::subtract)
				.subscribe(point -> {
					stage.setX(point.getX());
					stage.setY(point.getY());
				})
		);
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		this.resizeButton.autosize();

		Bounds layoutBounds = this.resizeButton.getLayoutBounds();
		this.resizeButton.setLayoutX(this.getWidth() - layoutBounds.getWidth());
		this.resizeButton.setLayoutY(this.getHeight() - layoutBounds.getHeight());
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
