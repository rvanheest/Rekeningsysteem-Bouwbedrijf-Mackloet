package com.github.rvanheest.rekeningsysteem.model.mutation;

import com.github.rvanheest.rekeningsysteem.model.document.ListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;

public final class MutationListItem implements ListItem {

  private final String description;
  private final String itemId;
  private final MonetaryAmount amount;

  public MutationListItem(String description, String itemId, MonetaryAmount amount) {
    this.description = description;
    this.itemId = itemId;
    this.amount = amount;
  }

  public String getDescription() {
    return this.description;
  }

  public String getItemId() {
    return this.itemId;
  }

  @Override
  public CurrencyUnit getCurrency() {
    return this.amount.getCurrency();
  }

  @Override
  public MonetaryAmount getWage() {
    return Money.zero(amount.getCurrency());
  }

  @Override
  public MonetaryAmount getMaterialCosts() {
    return this.amount;
  }

  public MonetaryAmount getTotal() {
    return this.getMaterialCosts();
  }

  @Override
  public <T> T accept(ListItemVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof MutationListItem) {
      MutationListItem that = (MutationListItem) other;
      return Objects.equals(this.description, that.description)
          && Objects.equals(this.itemId, that.itemId)
          && Objects.equals(this.amount, that.amount);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.description, this.itemId, this.amount);
  }

  @Override
  public String toString() {
    return "<MutationListItem[" + String.valueOf(this.description) + ", "
        + String.valueOf(this.itemId) + ", "
        + String.valueOf(this.amount) + "]>";
  }
}
