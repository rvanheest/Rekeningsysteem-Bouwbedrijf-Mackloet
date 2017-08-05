package com.github.rvanheest.rekeningsysteem.model.document;

import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitable;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

public interface ListItem extends ListItemVisitable {

  CurrencyUnit getCurrency();

  MonetaryAmount getWage();

  MonetaryAmount getMaterialCosts();
}
