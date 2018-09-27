package com.github.rvanheest.rekeningsysteem.businesslogic.model;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.ListItem;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public interface ItemListManager<E extends ListItem> {

  // TODO make this method protected in Java 9
  BehaviorSubject<ItemList<E>> itemList();

  default Completable addToItemList(E item) {
    try {
      BehaviorSubject<ItemList<E>> itemList = this.itemList();

      ItemList<E> newItemList = itemList.getValue().clone();
      newItemList.add(item);

      itemList.onNext(newItemList);
      return Completable.complete();
    }
    catch (DifferentCurrencyException | RuntimeException e) {
      return Completable.error(e);
    }
  }

  default Completable removeFromItemList(E item) {
    try {
      BehaviorSubject<ItemList<E>> itemList = this.itemList();

      ItemList<E> newItemList = itemList.getValue().clone();
      newItemList.remove(item);

      itemList.onNext(newItemList);
      return Completable.complete();
    }
    catch (RuntimeException e) {
      return Completable.error(e);
    }
  }

  default Completable clearItemList() {
    try {
      BehaviorSubject<ItemList<E>> itemList = this.itemList();

      ItemList<E> newItemList = itemList.getValue().clone();
      newItemList.clear();

      itemList.onNext(newItemList);
      return Completable.complete();
    }
    catch (RuntimeException e) {
      return Completable.error(e);
    }
  }

  default Observable<ItemList<E>> getItemList() {
    return this.itemList().distinctUntilChanged();
  }
}
