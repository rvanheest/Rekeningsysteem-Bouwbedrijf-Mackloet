package com.github.rvanheest.rekeningsysteem.test.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.ui.lib.searchbox.SearchBoxPresenter;
import io.reactivex.Observable;

import java.util.List;

class TestSearchBoxPresenter extends SearchBoxPresenter<String> {

  TestSearchBoxPresenter(SearchEngine<String> searchEngine) {
    super(searchEngine);
  }

  @Override
  public Observable<List<String>> viewStateObservable() {
    return super.viewStateObservable();
  }
}
