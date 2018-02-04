package com.github.rvanheest.rekeningsysteem.ui.lib.searchbox;

import com.github.rvanheest.rekeningsysteem.ui.lib.mvi.View;
import io.reactivex.Observable;

import java.util.List;

public interface SearchBoxView<T> extends View {

  Observable<String> textTypedIntent();

  Observable<T> selectedItemIntent();

  void render(List<T> viewState);
}
