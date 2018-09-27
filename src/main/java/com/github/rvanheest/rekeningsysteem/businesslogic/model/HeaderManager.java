package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import java.time.LocalDate;

public interface HeaderManager {

  // TODO make this method protected in Java 9
  BehaviorSubject<Header> header();

  default Completable withDebtor(Debtor debtor) {
    BehaviorSubject<Header> header = this.header();
    Header oldHeader = header.getValue();
    Header newHeader = new Header(debtor, oldHeader.getDate(), oldHeader.getInvoiceNumber());

    header.onNext(newHeader);
    return Completable.complete();
  }

  default Completable withDate(LocalDate date) {
    BehaviorSubject<Header> header = this.header();
    Header oldHeader = header.getValue();
    Header newHeader = new Header(oldHeader.getDebtor(), date, oldHeader.getInvoiceNumber());

    header.onNext(newHeader);
    return Completable.complete();
  }

  default Completable withInvoiceNumber(String invoiceNumber) {
    BehaviorSubject<Header> header = this.header();
    Header oldHeader = header.getValue();
    Header newHeader = new Header(oldHeader.getDebtor(), oldHeader.getDate(), invoiceNumber);

    header.onNext(newHeader);
    return Completable.complete();
  }

  default Observable<Header> getHeader() {
    return this.header().distinctUntilChanged();
  }
}
