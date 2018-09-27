package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.util.Optional;

public interface DebtorManager {

  // TODO make this method protected in Java 9
  BehaviorSubject<Debtor> debtor();

  default Completable withDebtorName(String name) {
    BehaviorSubject<Debtor> debtor = this.debtor();
    Debtor oldDebtor = debtor.getValue();
    Debtor newDebtor = new Debtor(oldDebtor.getDebtorID(), name, oldDebtor.getStreet(), oldDebtor.getNumber(),
        oldDebtor.getZipcode(), oldDebtor.getCity(), oldDebtor.getVatNumber());

    debtor.onNext(newDebtor);
    return Completable.complete();
  }

  default Completable withDebtorStreet(String street) {
    BehaviorSubject<Debtor> debtor = this.debtor();
    Debtor oldDebtor = debtor.getValue();
    Debtor newDebtor = new Debtor(oldDebtor.getDebtorID(), oldDebtor.getName(), street, oldDebtor.getNumber(),
        oldDebtor.getZipcode(), oldDebtor.getCity(), oldDebtor.getVatNumber());

    debtor.onNext(newDebtor);
    return Completable.complete();
  }

  default Completable withDebtorNumber(String number) {
    BehaviorSubject<Debtor> debtor = this.debtor();
    Debtor oldDebtor = debtor.getValue();
    Debtor newDebtor = new Debtor(oldDebtor.getDebtorID(), oldDebtor.getName(), oldDebtor.getStreet(), number,
        oldDebtor.getZipcode(), oldDebtor.getCity(), oldDebtor.getVatNumber());

    debtor.onNext(newDebtor);
    return Completable.complete();
  }

  default Completable withDebtorZipcode(String zipcode) {
    BehaviorSubject<Debtor> debtor = this.debtor();
    Debtor oldDebtor = debtor.getValue();
    Debtor newDebtor = new Debtor(oldDebtor.getDebtorID(), oldDebtor.getName(), oldDebtor.getStreet(),
        oldDebtor.getNumber(), zipcode, oldDebtor.getCity(), oldDebtor.getVatNumber());

    debtor.onNext(newDebtor);
    return Completable.complete();
  }

  default Completable withDebtorCity(String city) {
    BehaviorSubject<Debtor> debtor = this.debtor();
    Debtor oldDebtor = debtor.getValue();
    Debtor newDebtor = new Debtor(oldDebtor.getDebtorID(), oldDebtor.getName(), oldDebtor.getStreet(),
        oldDebtor.getNumber(), oldDebtor.getZipcode(), city, oldDebtor.getVatNumber());

    debtor.onNext(newDebtor);
    return Completable.complete();
  }

  default Completable withDebtorVatNumber(Optional<String> vatNumber) {
    BehaviorSubject<Debtor> debtor = this.debtor();
    Debtor oldDebtor = debtor.getValue();
    Debtor newDebtor = new Debtor(oldDebtor.getDebtorID(), oldDebtor.getName(), oldDebtor.getStreet(),
        oldDebtor.getNumber(), oldDebtor.getZipcode(), oldDebtor.getCity(), vatNumber);

    debtor.onNext(newDebtor);
    return Completable.complete();
  }

  default Observable<Debtor> getDebtor() {
    return this.debtor().distinctUntilChanged();
  }
}
