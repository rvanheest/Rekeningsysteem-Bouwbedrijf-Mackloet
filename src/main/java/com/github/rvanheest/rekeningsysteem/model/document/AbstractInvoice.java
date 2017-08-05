package com.github.rvanheest.rekeningsysteem.model.document;

import com.github.rvanheest.rekeningsysteem.model.document.header.Header;
import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import com.github.rvanheest.rekeningsysteem.model.totals.TotalsManager;

import java.util.Objects;

public abstract class AbstractInvoice<E extends ListItem> extends AbstractDocument implements TotalsManager {

  private final ItemList<E> itemList;

  public AbstractInvoice(Header header, ItemList<E> itemList) {
    super(header);
    this.itemList = itemList;
  }

  public ItemList<E> getItemList() {
    return itemList;
  }

  @Override
  public Totals getTotals() {
    return this.itemList.getTotals();
  }

  @Override
  public boolean equals(Object other) {
    if (super.equals(other) && other instanceof AbstractInvoice) {
      AbstractInvoice<?> that = (AbstractInvoice<?>) other;
      return super.equals(other)
          && Objects.equals(this.itemList, that.itemList);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.itemList);
  }
}
