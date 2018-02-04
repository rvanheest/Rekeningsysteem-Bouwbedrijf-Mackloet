package com.github.rvanheest.rekeningsysteem.ui.lib.mvi;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;

public class DisposableIntentObserver<Intent> extends DisposableObserver<Intent> {

  private final PublishSubject<Intent> subject;

  public DisposableIntentObserver(PublishSubject<Intent> subject) {
    this.subject = subject;
  }

  @Override
  public void onNext(Intent intent) {
    subject.onNext(intent);
  }

  @Override
  public void onError(Throwable e) {
    throw new IllegalStateException("View intents must not throw errors", e);
  }

  @Override
  public void onComplete() {
    subject.onComplete();
  }
}
