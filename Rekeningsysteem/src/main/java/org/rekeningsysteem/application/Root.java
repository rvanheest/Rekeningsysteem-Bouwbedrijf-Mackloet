package org.rekeningsysteem.application;

import java.util.Objects;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.rekeningsysteem.application.working.MainPane;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class Root extends BorderPane {

	private final UpperBar upperBar = new UpperBar();
	private final WindowResizeButton resizeButton;

	private final PublishSubject<Rectangle2D> windowBounds = PublishSubject.create();
	private final BehaviorSubject<Boolean> max;

	public Root(Stage stage, Database database) {
		this.setId("root");

		Boolean fullscreen = PropertiesWorker.getInstance()
				.getProperty(PropertyModelEnum.FULL_SCREEN)
				.map(Boolean::parseBoolean)
				.orElse(false);
		this.max = BehaviorSubject.create(fullscreen);

		this.setTop(this.upperBar);
		this.setCenter(new MainPane(stage, database));

		this.resizeButton = new WindowResizeButton(stage, Main.screenWidth, Main.screenHeight);
		this.resizeButton.setManaged(false);

		this.upperBar.getCloseButtonEvents()
				.subscribe(e -> Platform.exit());
		this.upperBar.getMinButtonEvents()
				.subscribe(e -> stage.setIconified(true));
		this.upperBar.getMaxButtonEvents()
				.map(e -> false).subscribe(this.max);

		Observable<Boolean> maxToggle = this.max.scan(!fullscreen, (cum, b) -> !cum)
				.skip(1)
				.doOnNext(b -> PropertiesWorker.getInstance()
						.setProperty(PropertyModelEnum.FULL_SCREEN, Boolean.toString(b)));
		maxToggle.filter(b -> b).subscribe(b -> {
			Platform.runLater(() -> {
				double x = stage.getX();
				double y = stage.getY();
				double width = stage.getWidth();
				double height = stage.getHeight();
				this.windowBounds.onNext(new Rectangle2D(x, y, width, height));

				Rectangle2D screen = Screen.getScreensForRectangle(x,
						y, 1, 1).get(0).getVisualBounds();
				stage.setX(screen.getMinX());
				stage.setY(screen.getMinY());
				stage.setWidth(screen.getWidth());
				stage.setHeight(screen.getHeight());
			});
		});
		maxToggle.filter(b -> !b)
				.withLatestFrom(this.windowBounds, (b, windowBounds) -> windowBounds)
				.subscribe(bounds -> {
					stage.setX(bounds.getMinX());
					stage.setY(bounds.getMinY());
					stage.setWidth(bounds.getWidth());
					stage.setHeight(bounds.getHeight());
				});
		Action1<WindowResizeButton> add = this.getChildren()::add;
		Action1<WindowResizeButton> remove = this.getChildren()::remove;
		maxToggle.map(b -> b ? remove : add)
				.forEach(action -> action.call(this.resizeButton));

		Observables.fromNodeEvents(this.upperBar, MouseEvent.MOUSE_CLICKED)
				.filter(event -> event.getClickCount() == 2)
				.doOnNext(MouseEvent::consume)
				.subscribe(event -> this.max.onNext(false));

		Observable<Point2D> pressed = Observables
				.fromNodeEvents(this.upperBar, MouseEvent.MOUSE_PRESSED)
				.doOnNext(MouseEvent::consume)
				.withLatestFrom(maxToggle, (event, maximized) -> maximized ? null : event)
				.filter(Objects::nonNull)
				.filter(event -> event.getClickCount() == 1)
				.map(event -> new Point2D(event.getSceneX(), event.getSceneY()));
		Observable<Point2D> dragged = Observables
				.fromNodeEvents(this.upperBar, MouseEvent.MOUSE_DRAGGED)
				.doOnNext(MouseEvent::consume)
				.withLatestFrom(maxToggle, (event, maximized) -> maximized ? null : event)
				.filter(Objects::nonNull)
				.filter(event -> event.getClickCount() == 1)
				.map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
		Observable.combineLatest(dragged, pressed.sample(dragged), Point2D::subtract)
				.doOnNext(point -> stage.setX(point.getX()))
				.doOnNext(point -> stage.setY(point.getY()))
				.subscribe();
	}

	@Override
	protected void layoutChildren() {
		super.layoutChildren();

		this.resizeButton.autosize();

		Bounds layoutBounds = this.resizeButton.getLayoutBounds();
		this.resizeButton.setLayoutX(this.getWidth() - layoutBounds.getWidth());
		this.resizeButton.setLayoutY(this.getHeight() - layoutBounds.getHeight());
	}
}
