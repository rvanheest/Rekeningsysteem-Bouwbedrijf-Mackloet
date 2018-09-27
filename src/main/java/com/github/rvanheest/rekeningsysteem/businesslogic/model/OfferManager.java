package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.offer.Offer;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class OfferManager extends AbstractDocumentManager<Offer> {

  private final BehaviorSubject<String> text = BehaviorSubject.create();
  private final BehaviorSubject<Boolean> sign = BehaviorSubject.create();

  public OfferManager(Offer defaultOffer) {
    super(defaultOffer);
    this.text.onNext(defaultOffer.getText());
    this.sign.onNext(defaultOffer.isSign());
  }

  @Override
  public Observable<Offer> getDocument() {
    return Observable.combineLatest(this.getHeader(), this.getText(), this.getSign(), Offer::new)
        .distinctUntilChanged();
  }

  public Completable withText(String text) {
    this.text.onNext(text);
    return Completable.complete();
  }

  public Observable<String> getText() {
    return this.text;
  }

  public Completable withSign(boolean sign) {
    this.sign.onNext(sign);
    return Completable.complete();
  }

  public Observable<Boolean> getSign() {
    return this.sign;
  }
}
