package com.github.rvanheest.rekeningsysteem.ui.lib.mvi;

import io.reactivex.Observable;

@FunctionalInterface
public interface ViewIntentBinder<V extends View, Intent> {

  Observable<Intent> apply(V view);
}
