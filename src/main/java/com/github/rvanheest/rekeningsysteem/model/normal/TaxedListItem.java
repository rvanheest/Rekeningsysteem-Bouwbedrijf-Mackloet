package com.github.rvanheest.rekeningsysteem.model.normal;

import com.github.rvanheest.rekeningsysteem.model.document.ListItem;

import javax.money.MonetaryAmount;

public interface TaxedListItem extends ListItem {

  double getWageTaxPercentage();

  double getMaterialCostsTaxPercentage();

  default MonetaryAmount getWageTax() {
    return this.getWage().multiply(this.getWageTaxPercentage()).divide(100L);
  }

  default MonetaryAmount getMaterialCostsTax() {
    return this.getMaterialCosts().multiply(this.getMaterialCostsTaxPercentage()).divide(100);
  }
}
