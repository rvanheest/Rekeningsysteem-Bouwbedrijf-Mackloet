package org.rekeningsysteem.application;

import org.rekeningsysteem.application.guice.ApplicationModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch();
	}

	public static String getResource(String s) {
		return Main.class.getResource(s).toExternalForm();
	}

	@Override
	public void start(Stage stage) {
		Injector injector = Guice.createInjector(new ApplicationModule(), new AbstractModule() {

			@Override
			protected void configure() {
				this.bind(Stage.class).toInstance(stage);
			}
		});
		Root root = injector.getInstance(Root.class);
		
		StackPane layerPane = new StackPane(root);

		Scene scene = new Scene(layerPane, 1061, 728);
		scene.getStylesheets().add(getResource("/layout.css"));

		stage.getIcons().add(new Image(getResource("/images/icon.gif")));
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Rekeningsysteem Mackloet");
		stage.show();
	}
}
