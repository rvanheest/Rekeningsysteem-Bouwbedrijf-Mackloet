package com.github.rvanheest.rekeningsysteem.ui.debtor;

import com.github.rvanheest.rekeningsysteem.businesslogic.DependencyInjection;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchInfoBox;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBox;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import io.reactivex.Observable;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class DebtorSearchBox extends SearchBox<Debtor> {

  public DebtorSearchBox() {
    super("Zoek debiteur...");
  }

  @Override
  public Observable<String> textTypedIntent() {
    return super.textTypedIntent().filter(s -> s.length() > 2);
  }

  @Override
  protected SearchBoxPresenter<Debtor> createPresenter() {
    return DependencyInjection.getInstance().newDebtorSearchBoxPresenter();
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
