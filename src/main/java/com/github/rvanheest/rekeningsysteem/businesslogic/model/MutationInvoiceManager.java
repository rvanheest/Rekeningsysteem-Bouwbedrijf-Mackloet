package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationInvoice;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class MutationInvoiceManager extends AbstractDocumentManager<MutationInvoice>
    implements ItemListManager<MutationListItem> {

  private final BehaviorSubject<ItemList<MutationListItem>> itemList = BehaviorSubject.create();

  public MutationInvoiceManager(MutationInvoice defaultInvoice) {
    super(defaultInvoice);
    this.itemList.onNext(defaultInvoice.getItemList());
  }

  @Override
  public Observable<MutationInvoice> getDocument() {
    return Observable.combineLatest(this.getHeader(), this.getItemList(), MutationInvoice::new)
        .distinctUntilChanged();
  }

  @Override
  public BehaviorSubject<ItemList<MutationListItem>> itemList() {
    return this.itemList;
  }
}
