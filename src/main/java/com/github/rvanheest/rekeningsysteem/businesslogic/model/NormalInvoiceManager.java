package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalInvoice;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class NormalInvoiceManager extends AbstractDocumentManager<NormalInvoice>
    implements ItemListManager<NormalListItem> {

  private final BehaviorSubject<String> description = BehaviorSubject.create();
  private final BehaviorSubject<ItemList<NormalListItem>> itemList = BehaviorSubject.create();

  public NormalInvoiceManager(NormalInvoice defaultInvoice) {
    super(defaultInvoice);
    this.description.onNext(defaultInvoice.getDescription());
    this.itemList.onNext(defaultInvoice.getItemList());
  }

  @Override
  public Observable<NormalInvoice> getDocument() {
    return Observable.combineLatest(this.getHeader(), this.getDescription(), this.getItemList(), NormalInvoice::new)
        .distinctUntilChanged();
  }

  public BehaviorSubject<String> description() {
    return this.description;
  }

  public Completable withDescription(String description) {
    this.description.onNext(description);
    return Completable.complete();
  }

  public Observable<String> getDescription() {
    return this.description;
  }

  @Override
  public BehaviorSubject<ItemList<NormalListItem>> itemList() {
    return this.itemList;
  }
}
