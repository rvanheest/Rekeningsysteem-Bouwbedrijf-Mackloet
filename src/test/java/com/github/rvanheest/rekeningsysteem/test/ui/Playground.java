package com.github.rvanheest.rekeningsysteem.test.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public abstract class Playground extends Application {

  protected void setUp() {}
  protected void tearDown() {}

  protected abstract Parent uiElement();

  @Override
  public void start(Stage stage) {
    this.setUp();
    Parent content = this.uiElement();

    Scene scene = new Scene(content);
    scene.getStylesheets().addAll(
        "searchbox.css",
        "section.css"
    );
    stage.setScene(scene);
    stage.initStyle(StageStyle.UNDECORATED);
    stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> this.tearDown());
    stage.show();
  }
}
