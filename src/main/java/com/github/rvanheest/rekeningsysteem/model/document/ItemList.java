package com.github.rvanheest.rekeningsysteem.model.document;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import com.github.rvanheest.rekeningsysteem.model.totals.TotalsListItemVisitor;
import com.github.rvanheest.rekeningsysteem.model.totals.TotalsManager;

import javax.money.CurrencyUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ItemList<E extends ListItem> implements TotalsManager {

  private final List<E> list;
  private final TotalsListItemVisitor visitor;
  private final CurrencyUnit currency;

  public ItemList(CurrencyUnit currency) {
    this(currency, new TotalsListItemVisitor());
  }

  public ItemList(CurrencyUnit currency, TotalsListItemVisitor visitor) {
    this.list = new ArrayList<>();
    this.currency = currency;
    this.visitor = visitor;
  }

  public ItemList(CurrencyUnit currency, Collection<? extends E> collection) throws DifferentCurrencyException {
    this(currency, new TotalsListItemVisitor(), collection);
  }

  public ItemList(CurrencyUnit currency, TotalsListItemVisitor visitor, Collection<? extends E> collection)
      throws DifferentCurrencyException {
    this(currency, visitor);
    for (E e : collection) {
      this.add(e);
    }
  }

  public List<E> getList() {
    return Collections.unmodifiableList(this.list);
  }

  public void add(E item) throws DifferentCurrencyException {
    CurrencyUnit itemCurrency = item.getCurrency();
    if (itemCurrency.equals(this.currency))
      list.add(item);
    else
      throw new DifferentCurrencyException(this.currency, itemCurrency);
  }

  @Override
  public Totals getTotals() {
    return this.list.parallelStream()
        .reduce(new Totals(this.currency), (totals, listItem) -> listItem.accept(this.visitor).apply(totals), Totals::add);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof ItemList<?>) {
      ItemList<?> that = (ItemList<?>) other;
      return Objects.equals(this.currency, that.currency)
          && Objects.equals(this.list, that.list);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.currency, this.list);
  }
}
