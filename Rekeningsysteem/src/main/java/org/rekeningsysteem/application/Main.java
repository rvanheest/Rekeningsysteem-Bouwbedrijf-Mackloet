package org.rekeningsysteem.application;

import java.sql.SQLException;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import org.rekeningsysteem.application.versioning.VersionControl;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;

public class Main extends Application {

	private static Main main;

	private final StackPane popup = new StackPane();

	public static void main(String[] args) {
		Application.launch();
	}

	public static Main getMain() {
		return main;
	}

	public static String getResource(String s) {
		return Main.class.getResource(s).toExternalForm();
	}

	@Override
	public void start(Stage stage) {
		try {
			Database database = Database.getInstance();
			VersionControl vc = new VersionControl(database);
			vc.checkDBVersioning().subscribe();

			main = this;
			this.popup.setId("modalDimmer");

			Observable.merge(Observables.fromNodeEvents(this.popup, MouseEvent.MOUSE_CLICKED),
					Observables.fromNodeEvents(this.popup, KeyEvent.KEY_PRESSED)
							.filter(event -> event.getCode() == KeyCode.ESCAPE))
					.doOnNext(Event::consume)
					.forEach(event -> this.hideModalMessage());

			this.popup.setVisible(false);

			StackPane layerPane = new StackPane(new Root(stage), this.popup);

			Scene scene = new Scene(layerPane, 1061, 728);
			scene.getStylesheets().add(getResource("/layout.css"));

			stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> {
				try {
					Database.closeInstance();
				}
				catch (SQLException e) {
					ApplicationLogger.getInstance().error("Could not close database.", e);
				}
			});
			stage.getIcons().add(new Image(getResource("/images/icon.gif")));
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setTitle("Rekeningsysteem Mackloet");
			stage.show();
		}
		catch (Exception e) {
			ApplicationLogger.getInstance().error("Exception caught on toplevel", e);
		}
	}

	public void showModalMessage(Node message) {
		this.popup.getChildren().add(message);
		this.popup.setOpacity(0);
		this.popup.setVisible(true);
		this.popup.setCache(true);

		KeyValue kv = new KeyValue(this.popup.opacityProperty(), 1, Interpolator.EASE_BOTH);
		KeyFrame kf = new KeyFrame(Duration.millis(250),
				event -> this.popup.setCache(false), kv);
		Timeline timeline = new Timeline(kf);
		timeline.play();
	}

	public void hideModalMessage() {
		this.popup.setCache(true);
		KeyValue kv = new KeyValue(this.popup.opacityProperty(), 0, Interpolator.EASE_BOTH);
		KeyFrame kf = new KeyFrame(Duration.millis(250), event -> {
			this.popup.setCache(false);
			this.popup.setVisible(false);
			this.popup.getChildren().clear();
		}, kv);

		Timeline line = new Timeline(kf);
		line.play();
	}
}
