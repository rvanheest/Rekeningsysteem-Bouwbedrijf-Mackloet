package com.github.rvanheest.rekeningsysteem.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.businesslogic.model.HeaderManager;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBox;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchInfoBox;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class DebtorSearchBox extends SearchBox<Debtor> {

  public DebtorSearchBox(SearchEngine<Debtor> searchEngine, HeaderManager headerManager) {
    super("Zoek debiteur...", new DebtorSearchBoxPresenter(searchEngine, headerManager));
  }

  @Override
  protected Node displaySuggestion(Debtor suggestion) {
    return new Label(suggestion.getName());
  }

  @Override
  protected SearchInfoBox<Debtor> createInfoBox() {
    return new DebtorInfoBox();
  }
}
