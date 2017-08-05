package com.github.rvanheest.rekeningsysteem.model.normal;

import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;

public class SimpleListItem extends NormalListItem {

  private final MonetaryAmount amount;
  private final double taxPercentage;

  public SimpleListItem(String description, MonetaryAmount amount, double taxPercentage) {
    super(description);
    this.amount = amount;
    this.taxPercentage = taxPercentage;
  }

  @Override
  public CurrencyUnit getCurrency() {
    return this.amount.getCurrency();
  }

  @Override
  public MonetaryAmount getMaterialCosts() {
    return this.amount;
  }

  @Override
  public double getMaterialCostsTaxPercentage() {
    return this.taxPercentage;
  }

  @Override
  public MonetaryAmount getWage() {
    return Money.zero(this.getCurrency());
  }

  @Override
  public double getWageTaxPercentage() {
    return 0;
  }

  @Override
  public MonetaryAmount getWageTax() {
    return this.getWage();
  }

  @Override
  public <T> T accept(ListItemVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof SimpleListItem) {
      SimpleListItem that = (SimpleListItem) other;
      return super.equals(that)
          && Objects.equals(this.amount, that.amount)
          && Objects.equals(this.taxPercentage, that.taxPercentage);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.amount, this.taxPercentage);
  }

  @Override
  public String toString() {
    return "<SimpleListItem[" + String.valueOf(this.getDescription()) + ", "
        + String.valueOf(this.amount) + ", "
        + String.valueOf(this.taxPercentage) + "]>";
  }
}
