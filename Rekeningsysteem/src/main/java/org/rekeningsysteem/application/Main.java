package org.rekeningsysteem.application;

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
		StackPane layerPane = new StackPane(new Root(stage));

		Scene scene = new Scene(layerPane, 1061, 728);
		scene.getStylesheets().add(getResource("/layout.css"));

		stage.getIcons().add(new Image(getResource("/images/icon.gif")));
		stage.setScene(scene);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setTitle("Rekeningsysteem Mackloet");
		stage.show();
	}
}
