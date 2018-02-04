package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class InfoBox<T> extends VBox {

  public InfoBox() {
    this.setId("search-info-box");

    this.setMinHeight(Region.USE_PREF_SIZE);
    this.setPrefWidth(200);
  }

  public abstract void setContent(T t);
}
