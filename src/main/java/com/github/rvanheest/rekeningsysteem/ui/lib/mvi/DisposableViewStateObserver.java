package com.github.rvanheest.rekeningsysteem.ui.lib.mvi;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.BehaviorSubject;

public class DisposableViewStateObserver<T> extends DisposableObserver<T> {

  private BehaviorSubject<T> viewStateBehaviorSubject;

  public DisposableViewStateObserver(BehaviorSubject<T> viewStateBehaviorSubject) {
    this.viewStateBehaviorSubject = viewStateBehaviorSubject;
  }

  @Override
  public void onNext(T t) {
    this.viewStateBehaviorSubject.onNext(t);
  }

  @Override
  public void onError(Throwable e) {
    throw new IllegalStateException("ViewState Observable must never reach error state - onError()", e);
  }

  @Override
  public void onComplete() {
    // ViewState Observable never completes so ignore any complete event
  }
}
