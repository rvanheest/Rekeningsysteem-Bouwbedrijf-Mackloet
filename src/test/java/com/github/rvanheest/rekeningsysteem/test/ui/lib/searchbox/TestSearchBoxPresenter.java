package com.github.rvanheest.rekeningsysteem.test.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxViewState;
import io.reactivex.Observable;

class TestSearchBoxPresenter extends SearchBoxPresenter<String> {

  TestSearchBoxPresenter(SearchEngine<String> searchEngine) {
    super(searchEngine);
  }

  @Override
  public Observable<SearchBoxViewState<String>> viewStateObservable() {
    return super.viewStateObservable();
  }
}
