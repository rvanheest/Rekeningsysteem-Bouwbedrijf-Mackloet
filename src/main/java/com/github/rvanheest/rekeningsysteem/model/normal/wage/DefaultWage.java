package com.github.rvanheest.rekeningsysteem.model.normal.wage;

import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.Objects;

public final class DefaultWage extends AbstractWage {

  private final MonetaryAmount wage;
  private final double wageTaxPercentage;

  public DefaultWage(String description, MonetaryAmount wage, double wageTaxPercentage) {
    super(description);
    this.wage = wage;
    this.wageTaxPercentage = wageTaxPercentage;
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
  public double getWageTaxPercentage() {
    return this.wageTaxPercentage;
  }

  @Override
  public <T> T accept(ListItemVisitor<T> visitor) {
    return visitor.visit(this);
  }

  @Override
  public boolean equals(Object other) {
    if (super.equals(other) && other instanceof DefaultWage) {
      DefaultWage that = (DefaultWage) other;
      return Objects.equals(this.wage, that.wage)
          && Objects.equals(this.wageTaxPercentage, that.wageTaxPercentage);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), this.wage, this.wageTaxPercentage);
  }

  @Override
  public String toString() {
    return "<DefaultWage[" + String.valueOf(this.getDescription()) + ", "
        + String.valueOf(this.wage) + ", "
        + String.valueOf(this.wageTaxPercentage) + "]>";
  }
}
