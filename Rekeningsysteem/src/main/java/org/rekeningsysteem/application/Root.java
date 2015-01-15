package org.rekeningsysteem.application;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import org.rekeningsysteem.application.working.MainPane;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class Root extends BorderPane {

	private final UpperBar upperBar = new UpperBar();
	private final WindowResizeButton resizeButton;

	private Rectangle2D backupWindowBounds;
	private boolean maximized = false;
	private final BehaviorSubject<Boolean> max;

	public Root(Stage stage) {
		this.setId("root");

		Boolean fullscreen = PropertiesWorker.getInstance()
				.getProperty(PropertyModelEnum.FULL_SCREEN)
				.map(Boolean::parseBoolean)
				.orElse(false);
		this.max = BehaviorSubject.create(fullscreen);

		this.setTop(this.upperBar);
		this.setCenter(new MainPane(stage));

		this.resizeButton = new WindowResizeButton(stage, 1061, 728);
		this.resizeButton.setManaged(false);

		this.upperBar.getCloseButtonEvents()
				.subscribe(e -> Platform.exit());
		this.upperBar.getMinButtonEvents()
				.subscribe(e -> stage.setIconified(true));
		this.upperBar.getMaxButtonEvents()
				.map(e -> false).subscribe(this.max);

		Observable<Boolean> maxim = this.max.scan(!fullscreen, (cum, b) -> !cum)
				.skip(1)
				.doOnNext(b -> this.maximized = b)
				.doOnNext(b -> PropertiesWorker.getInstance()
						.setProperty(PropertyModelEnum.FULL_SCREEN, String.valueOf(b)));
		maxim.filter(b -> b)
				.subscribe(b -> {
					Platform.runLater(() -> {
						this.backupWindowBounds = new Rectangle2D(stage.getX(),
								stage.getY(), stage.getWidth(), stage.getHeight());

						Rectangle2D screen = Screen.getScreensForRectangle(stage.getX(),
								stage.getY(), 1, 1).get(0).getVisualBounds();
						stage.setX(screen.getMinX());
						stage.setY(screen.getMinY());
						stage.setWidth(screen.getWidth());
						stage.setHeight(screen.getHeight());
					});
				});
		maxim.filter(b -> !b)
				.map(b -> this.backupWindowBounds)
				.filter(bounds -> bounds != null)
				.subscribe(bounds -> {
					stage.setX(bounds.getMinX());
					stage.setY(bounds.getMinY());
					stage.setWidth(bounds.getWidth());
					stage.setHeight(bounds.getHeight());
				});

		Observables.fromNodeEvents(this.upperBar, MouseEvent.MOUSE_CLICKED)
				.filter(event -> event.getClickCount() == 2)
				.doOnNext(MouseEvent::consume)
				.subscribe(event -> this.max.onNext(false));

		Observable<Point2D> pressed = Observables
				.fromNodeEvents(this.upperBar, MouseEvent.MOUSE_PRESSED)
				.doOnNext(MouseEvent::consume)
				.filter(event -> !this.maximized)
				.filter(event -> event.getClickCount() == 1)
				.map(event -> new Point2D(event.getSceneX(), event.getSceneY()));
		Observable<Point2D> dragged = Observables
				.fromNodeEvents(this.upperBar, MouseEvent.MOUSE_DRAGGED)
				.doOnNext(MouseEvent::consume)
				.filter(event -> !this.maximized)
				.filter(event -> event.getClickCount() == 1)
				.map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
		Observable.combineLatest(dragged, pressed.sample(dragged), Point2D::subtract)
				.doOnNext(point -> stage.setX(point.getX()))
				.doOnNext(point -> stage.setY(point.getY()))
				.subscribe();

		maxim.map(b -> b ? this.getChildren().remove(this.resizeButton)
				: this.getChildren().add(this.resizeButton))
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
