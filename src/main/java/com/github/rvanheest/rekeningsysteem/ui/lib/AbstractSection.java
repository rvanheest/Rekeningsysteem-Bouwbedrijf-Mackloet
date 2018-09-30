package com.github.rvanheest.rekeningsysteem.ui.lib;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public abstract class AbstractSection extends VBox {

  protected AbstractSection(String headerText) {
    this.getStyleClass().add("section");

    Label header = new Label(headerText);
    header.setId("title");

    this.getChildren().add(header);
  }
}
