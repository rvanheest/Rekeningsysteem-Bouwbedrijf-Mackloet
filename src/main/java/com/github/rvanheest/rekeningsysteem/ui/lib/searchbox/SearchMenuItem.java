package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.scene.Node;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

class SearchMenuItem extends CustomMenuItem {

  private final Region region = new Region();

  SearchMenuItem(Node node) {
    this.getStyleClass().add("search-menu-item");
    this.region.getStyleClass().add("search-menu-item-popup-region");

    HBox hBox = new HBox(node, this.region);
    hBox.setFillHeight(true);

    this.setContent(hBox);
  }

  public Observable<Boolean> hover() {
    return JavaFxObservable.valuesOf(this.region.opacityProperty())
        .map(Number::doubleValue)
        .filter(d -> d == 1)
        .map(d -> true)
        .subscribeOn(JavaFxScheduler.platform()); // used subscribeOn here as a workaround for RT-14396
  }
}
