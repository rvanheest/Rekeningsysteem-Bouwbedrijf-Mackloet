package com.github.rvanheest.rekeningsysteem.ui.debtor;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchInfoBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.util.Optional;

public class DebtorInfoBox extends SearchInfoBox<Debtor> {

  private final Label infoName = new Label();
  private final Label infoAddress = new Label();
  private final Label infoZipcodeCity = new Label();
  private final Label infoVatNumber = new Label();
  private boolean infoVatNumberVisible = false;

  public DebtorInfoBox() {
    this.infoName.setId("search-info-name");
    this.infoAddress.setId("search-info-description");
    this.infoZipcodeCity.setId("search-info-description");
    this.infoVatNumber.setId("search-info-description");

    this.infoName.setMinHeight(Region.USE_PREF_SIZE);

    this.infoName.setPrefHeight(28);
    this.infoAddress.setPrefWidth(this.getPrefWidth() - 24);
    this.infoZipcodeCity.setPrefWidth(this.getPrefWidth() - 24);
    this.infoVatNumber.setPrefWidth(this.getPrefWidth() - 24);

    this.infoAddress.setWrapText(true);
    this.infoZipcodeCity.setWrapText(true);
    this.infoVatNumber.setWrapText(true);

    this.getChildren().addAll(this.infoName, this.infoAddress, this.infoZipcodeCity);
  }

  @Override
  public void setContent(Debtor debtor) {
    String name = debtor.getName();
    String streetNumber = String.format("%s %s", debtor.getStreet(), debtor.getNumber());
    String zipcodeCity = String.format("%s  %S", debtor.getZipcode(), debtor.getCity());
    Optional<String> maybeVatNumber = debtor.getVatNumber().map(s -> String.format("BTW nummer: %s", s));
    String vatNumber = maybeVatNumber.orElse("");

    this.infoName.setText(name);
    this.infoAddress.setText(streetNumber);
    this.infoZipcodeCity.setText(zipcodeCity);
    this.infoVatNumber.setText(vatNumber);

    if (maybeVatNumber.isPresent() && !this.infoVatNumberVisible) {
      this.getChildren().add(this.infoVatNumber);
      this.infoVatNumberVisible = true;
    }
    else if (!maybeVatNumber.isPresent() && this.infoVatNumberVisible) {
      this.getChildren().remove(this.infoVatNumber);
      this.infoVatNumberVisible = false;
    }
    // in other cases, do nothing
  }
}
