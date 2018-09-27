package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.document.AbstractDocument;
import com.github.rvanheest.rekeningsysteem.model.document.header.Debtor;
import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;

public abstract class AbstractDocumentManager<Doc extends AbstractDocument>
    implements DocumentManager<Doc>, Disposable {

  private final BehaviorSubject<Debtor> debtor = BehaviorSubject.create();
  private final BehaviorSubject<Header> header = BehaviorSubject.create();

  private final Disposable debtorToHeader;

  public AbstractDocumentManager(Doc defaultDoc) {
    this.debtor.onNext(defaultDoc.getHeader().getDebtor());
    this.header.onNext(defaultDoc.getHeader());

    this.debtorToHeader = this.debtor.subscribe(this::withDebtor);
  }

  @Override
  public BehaviorSubject<Header> header() {
    return this.header;
  }

  @Override
  public BehaviorSubject<Debtor> debtor() {
    return this.debtor;
  }

  @Override
  public void dispose() {
    if (!isDisposed())
      this.debtorToHeader.dispose();
  }

  @Override
  public boolean isDisposed() {
    return this.debtorToHeader.isDisposed();
  }
}
