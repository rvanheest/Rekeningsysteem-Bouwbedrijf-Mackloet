package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public interface HeaderWithDescriptionManager extends HeaderManager {

  // TODO make this method protected in Java 9
  BehaviorSubject<String> description();

  default Completable withDescription(String description) {
    this.description().onNext(description);

    return Completable.complete();
  }

  default Observable<String> getDescription() {
    return this.description().distinctUntilChanged();
  }
}
