package com.github.rvanheest.rekeningsysteem.model.normal;

import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;

public class EsselinkListItem extends NormalListItem {

  private final EsselinkItem item;
  private final double amount;
  private final double taxPercentage;

  public EsselinkListItem(EsselinkItem item, double amount, double taxPercentage) {
    this(item.getDescription(), item, amount, taxPercentage);
  }

  public EsselinkListItem(String description, EsselinkItem item, double amount, double taxPercentage) {
    super(description);
    this.taxPercentage= taxPercentage;
    this.item = item;
    this.amount = amount;
  }

  @Override
  public CurrencyUnit getCurrency() {
    return this.item.getPricePerUnit().getCurrency();
  }

  @Override
  public MonetaryAmount getMaterialCosts() {
    return this.item.getPricePerUnit().multiply(this.amount).divide(this.item.getAmountPer());
  }

  @Override
  public double getMaterialCostsTaxPercentage() {
    return this.taxPercentage;
  }

  @Override
  public MonetaryAmount getWage() {
    return Money.zero(this.item.getPricePerUnit().getCurrency());
  }

  @Override
  public double getWageTaxPercentage() {
    return 0;
  }

  @Override
  public MonetaryAmount getWageTax() {
    return this.getWage();
  }

  public EsselinkItem getItem() {
    return this.item;
  }

  public double getAmount() {
    return this.amount;
  }

  @Override
  public <T> T accept(ListItemVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof EsselinkListItem) {
      EsselinkListItem that = (EsselinkListItem) other;
      return super.equals(that)
          && Objects.equals(this.taxPercentage, that.taxPercentage)
          && Objects.equals(this.amount, that.amount)
          && Objects.equals(this.item, that.item);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.taxPercentage, this.amount, this.item);
  }

  @Override
  public String toString() {
    return "<EsselinkListItem[" + String.valueOf(this.getDescription()) + ", "
        + String.valueOf(this.taxPercentage) + ", "
        + String.valueOf(this.amount) + ", "
        + String.valueOf(this.item) + "]>";
  }
}
