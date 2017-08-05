package com.github.rvanheest.rekeningsysteem.exception;

import javax.money.CurrencyUnit;

public class DifferentCurrencyException extends Exception {

  public DifferentCurrencyException(CurrencyUnit c1, CurrencyUnit c2) {
    super("Currency " + c1 + " is not equal to currency " + c2);
  }
}
