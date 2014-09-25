package org.rekeningsysteem.application;

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
import javafx.util.Duration;

import org.rekeningsysteem.application.guice.ApplicationModule;
import org.rekeningsysteem.rxjavafx.Observables;

import rx.Observable;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Main extends Application {

	private static Main main;

	private final StackPane modalDimmer = new StackPane();

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
		main = this;
		this.modalDimmer.setId("modalDimmer");
		
		Observable.merge(Observables.fromNodeEvents(this.modalDimmer, MouseEvent.MOUSE_CLICKED),
				Observables.fromNodeEvents(this.modalDimmer, KeyEvent.KEY_PRESSED)
					.filter(event -> event.getCode() == KeyCode.ESCAPE))
				.doOnNext(Event::consume)
				.doOnNext(event -> this.hideModalMessage())
				.subscribe();
		
        this.modalDimmer.setVisible(false);
		
		Injector injector = Guice.createInjector(new ApplicationModule(), new AbstractModule() {

			@Override
			protected void configure() {
				this.bind(Stage.class).toInstance(stage);
			}
		});
		StackPane layerPane = new StackPane(injector.getInstance(Root.class), this.modalDimmer);

		Scene scene = new Scene(layerPane, 1061, 728);
		scene.getStylesheets().add(getResource("/layout.css"));

		stage.getIcons().add(new Image(getResource("/images/icon.gif")));
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Rekeningsysteem Mackloet");
		stage.show();
	}
	
	public void showModalMessage(Node message) {
		this.modalDimmer.getChildren().add(message);
		this.modalDimmer.setOpacity(0);
		this.modalDimmer.setVisible(true);
		this.modalDimmer.setCache(true);
		
		KeyValue kv = new KeyValue(this.modalDimmer.opacityProperty(), 1, Interpolator.EASE_BOTH);
		KeyFrame kf = new KeyFrame(Duration.millis(250), event -> this.modalDimmer.setCache(false), kv);
		Timeline timeline = new Timeline(kf);
		timeline.play();
	}
	
	public void hideModalMessage() {
		this.modalDimmer.setCache(true);
		KeyValue kv = new KeyValue(this.modalDimmer.opacityProperty(), 0, Interpolator.EASE_BOTH);
		KeyFrame kf = new KeyFrame(Duration.millis(250), event -> {
			this.modalDimmer.setCache(false);
			this.modalDimmer.setVisible(false);
			this.modalDimmer.getChildren().clear();
		}, kv);
		
		Timeline line = new Timeline(kf);
		line.play();
	}
}
