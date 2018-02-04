package com.github.rvanheest.rekeningsysteem.ui.lib.mvi;

import io.reactivex.disposables.Disposable;

public interface Presenter<V extends View> extends Disposable {

  void attachView(V view);
}
