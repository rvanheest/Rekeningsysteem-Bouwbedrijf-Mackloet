package org.rekeningsysteem.application;

import java.io.File;
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

import org.apache.logging.log4j.core.Logger;
import org.rekeningsysteem.application.versioning.VersionControl;
import org.rekeningsysteem.io.database.Database;
import org.rekeningsysteem.logging.ApplicationLogger;
import org.rekeningsysteem.properties.PropertiesWorker;
import org.rekeningsysteem.properties.PropertyModelEnum;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;
import rx.schedulers.Schedulers;

public class Main extends Application {

	private static Main main;
	public static final double screenWidth = 1061;
	public static final double screenHeight = 728;

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

	public static String getExternalResource(String path) {
		return new File(path).toURI().toString();
	}

	@Override
	public void start(Stage stage) {
		Logger logger = ApplicationLogger.getInstance();
		PropertiesWorker properties = PropertiesWorker.getInstance();
		try {
			Database database = Database.getInstance();
			VersionControl vc = new VersionControl(database);
			vc.checkDBVersioning().subscribeOn(Schedulers.io()).subscribe();

			main = this;
			this.popup.setId("modalDimmer");

			Observable.merge(Observables.fromNodeEvents(this.popup, MouseEvent.MOUSE_CLICKED),
					Observables.fromNodeEvents(this.popup, KeyEvent.KEY_PRESSED)
							.filter(event -> event.getCode() == KeyCode.ESCAPE))
					.doOnNext(Event::consume)
					.forEach(event -> this.hideModalMessage());

			this.popup.setVisible(false);

			StackPane layerPane = new StackPane(new Root(stage, database, logger), this.popup);

			Scene scene = new Scene(layerPane, screenWidth, screenHeight);
			scene.getStylesheets().add(getResource("/layout.css"));

			stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> {
				try {
					database.close();
				}
				catch (SQLException e) {
					logger.error("Could not close database.", e);
				}
			});
			properties.getProperty(PropertyModelEnum.APPLICATION_ICON)
				.map(path -> new Image(Main.getExternalResource(path)))
				.ifPresent(stage.getIcons()::add);
			stage.setScene(scene);
			stage.initStyle(StageStyle.UNDECORATED);
			properties.getProperty(PropertyModelEnum.APPLICATION_TITLE).ifPresent(stage::setTitle);
			stage.show();
		}
		catch (Exception e) {
			logger.error("Exception caught on toplevel", e);
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
