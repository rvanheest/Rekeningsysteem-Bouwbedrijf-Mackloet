package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;

import java.util.concurrent.TimeUnit;

public class SearchBoxPresenter<T> extends BasePresenter<SearchBoxView<T>, SearchBoxViewState<T>> {

  private final SearchEngine<T> searchEngine;

  public SearchBoxPresenter(SearchEngine<T> searchEngine) {
    this.searchEngine = searchEngine;
  }

  @Override
  protected void bindIntents() {
    Observable<SearchBoxViewState<T>> viewState = intent(SearchBoxView::textTypedIntent)
        .throttleWithTimeout(250, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .switchMap(this.searchEngine::suggest)
        .observeOn(JavaFxScheduler.platform())
        .map(SearchBoxViewState::new)
        .distinctUntilChanged();

    subscribeViewState(viewState, SearchBoxView::render);
  }
}
