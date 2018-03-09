package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.businesslogic.SearchEngine;
import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.BasePresenter;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchBoxPresenter<T> extends BasePresenter<SearchBoxView<T>, SearchBoxViewState<T>> {

  private final SearchEngine<T> searchEngine;

  public SearchBoxPresenter(SearchEngine<T> searchEngine) {
    this.searchEngine = searchEngine;
  }

  @Override
  protected void bindIntents() {
    Observable<List<T>> searchSuggestions = intent(SearchBoxView::textTypedIntent)
        .throttleWithTimeout(250, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .switchMap(this.searchEngine::suggest)
        .observeOn(JavaFxScheduler.platform());

    // TODO selectedItemIntent subscribe to InvoiceManager

    Observable<SearchBoxViewState<T>> viewState = searchSuggestions
        .map(SearchBoxViewState::new)
        .distinctUntilChanged();

    subscribeViewState(viewState, SearchBoxView::render);
  }

  @Override
  protected void unbindIntents() {
    super.unbindIntents();
    // TODO add disposing from selectedItemIntent->InvoiceManager here
  }
}
