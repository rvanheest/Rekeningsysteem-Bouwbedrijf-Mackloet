package com.github.rvanheest.rekeningsysteem.model.normal.wage;

import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;

public abstract class AbstractWage extends NormalListItem {

  public AbstractWage(String description) {
    super(description);
  }

  // AbstractWage and all its subclasses have no material costs, so it is fixed to zero
  @Override
  public final MonetaryAmount getMaterialCosts() {
    return Money.zero(getWage().getCurrency());
  }

  @Override
  public final double getMaterialCostsTaxPercentage() {
    return 0;
  }

  @Override
  public final MonetaryAmount getMaterialCostsTax() {
    return this.getMaterialCosts();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof AbstractWage) {
      AbstractWage that = (AbstractWage) other;
      return super.equals(that);
    }
    return false;
  }
}
