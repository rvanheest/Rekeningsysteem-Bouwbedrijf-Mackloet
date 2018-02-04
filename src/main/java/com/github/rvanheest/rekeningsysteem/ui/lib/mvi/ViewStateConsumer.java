package com.github.rvanheest.rekeningsysteem.ui.lib.mvi;

public interface ViewStateConsumer<V extends View, ViewState> {

  void accept(V view, ViewState viewState);
}
