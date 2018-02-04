package com.github.rvanheest.rekeningsysteem.test.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchInfoBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

class TestInfoBox extends SearchInfoBox<String> {

  private final Label infoName = new Label("my name");

  TestInfoBox() {
    super();

    Label infoAddress = new Label("street and number");
    Label infoZipcodeCity = new Label("zipcode and city");
    Label infoVatNumber = new Label("vat number");

    this.infoName.setId("search-info-name");
    infoAddress.setId("search-info-description");
    infoZipcodeCity.setId("search-info-description");
    infoVatNumber.setId("search-info-description");

    this.infoName.setPrefHeight(28);
    this.infoName.setMinHeight(Region.USE_PREF_SIZE);
    infoAddress.setPrefWidth(this.getPrefWidth() - 24);
    infoZipcodeCity.setPrefWidth(this.getPrefWidth() - 24);
    infoVatNumber.setPrefWidth(this.getPrefWidth() - 24);

    this.getChildren().addAll(this.infoName, infoAddress, infoZipcodeCity, infoVatNumber);
  }

  @Override
  public void setContent(String s) {
    this.infoName.setText(s);
  }
}
