package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import io.reactivex.Observable;

public interface SearchBoxView<T> extends View {

  Observable<String> textTypedIntent();

  Observable<Boolean> clearButtonIntent();

  Observable<Boolean> escapeTypedIntent();

  Observable<T> selectedItemIntent();

  void render(SearchBoxViewState<T> viewState);
}
