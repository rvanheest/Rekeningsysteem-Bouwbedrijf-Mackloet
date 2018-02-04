package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public abstract class SearchInfoBox<T> extends VBox {

  public SearchInfoBox() {
    this.setId("search-info-box");

    this.setMinHeight(Region.USE_PREF_SIZE);
    this.setPrefWidth(200);
  }

  public abstract void setContent(T t);
}
