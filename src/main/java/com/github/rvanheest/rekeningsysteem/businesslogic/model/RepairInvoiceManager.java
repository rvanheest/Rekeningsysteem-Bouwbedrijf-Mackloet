package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairInvoice;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class RepairInvoiceManager extends AbstractDocumentManager<RepairInvoice>
    implements ItemListManager<RepairListItem> {

  private final BehaviorSubject<ItemList<RepairListItem>> itemList = BehaviorSubject.create();

  public RepairInvoiceManager(RepairInvoice defaultInvoice) {
    super(defaultInvoice);
    this.itemList.onNext(defaultInvoice.getItemList());
  }

  @Override
  public Observable<RepairInvoice> getDocument() {
    return Observable.combineLatest(this.getHeader(), this.getItemList(), RepairInvoice::new)
        .distinctUntilChanged();
  }

  @Override
  public BehaviorSubject<ItemList<RepairListItem>> itemList() {
    return this.itemList;
  }
}
