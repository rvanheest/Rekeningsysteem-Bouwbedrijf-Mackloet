package com.github.rvanheest.rekeningsysteem.ui.lib;

import javafx.scene.layout.VBox;

public abstract class AbstractPage extends VBox {

  private final String title;

  protected AbstractPage(String title, AbstractSection... sections) {
    super(sections);
    this.getStyleClass().add("page");
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }
}
