package com.github.rvanheest.rekeningsysteem.model.repair;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;

public class RepairListItem implements ListItem {

  private final String description;
  private final String itemId;
  private final MonetaryAmount wage;
  private final MonetaryAmount materialCosts;
  private final MonetaryAmount total;

  public RepairListItem(String description, String itemId, MonetaryAmount wage, MonetaryAmount materialCosts)
      throws DifferentCurrencyException {
    CurrencyUnit wageCurrency = wage.getCurrency();
    CurrencyUnit materialCostsCurrency = materialCosts.getCurrency();
    if (wageCurrency.equals(materialCostsCurrency)) {
      this.description = description;
      this.itemId = itemId;
      this.wage = wage;
      this.materialCosts = materialCosts;
      this.total = this.wage.add(this.materialCosts);
    }
    else throw new DifferentCurrencyException(wageCurrency, materialCostsCurrency);
  }

  public String getDescription() {
    return this.description;
  }

  public String getItemId() {
    return this.itemId;
  }

  @Override
  public CurrencyUnit getCurrency() {
    return this.wage.getCurrency();
  }

  @Override
  public MonetaryAmount getWage() {
    return this.wage;
  }

  @Override
  public MonetaryAmount getMaterialCosts() {
    return this.materialCosts;
  }

  public MonetaryAmount getTotal() {
    return this.total;
  }

  @Override
  public <T> T accept(ListItemVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof RepairListItem) {
      RepairListItem that = (RepairListItem) other;
      return Objects.equals(this.description, that.description)
          && Objects.equals(this.itemId, that.itemId)
          && Objects.equals(this.wage, that.wage)
          && Objects.equals(this.materialCosts, that.materialCosts);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.description, this.itemId, this.wage, this.materialCosts);
  }

  @Override
  public String toString() {
    return "<RepairListItem[" + String.valueOf(this.description) + ", "
        + String.valueOf(this.itemId) + ", "
        + String.valueOf(this.wage) + ", "
        + String.valueOf(this.materialCosts) + "]>";
  }
}
