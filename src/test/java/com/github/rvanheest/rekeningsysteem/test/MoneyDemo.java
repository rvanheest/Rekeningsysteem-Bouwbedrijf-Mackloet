package com.github.rvanheest.rekeningsysteem.test;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class MoneyDemo {

  public void testMoney() {
    CurrencyUnit euro = Monetary.getCurrency("EUR");
    MonetaryAmount money = Money.of(1234567.346, euro);
    MonetaryOperator rounding = Monetary.getRounding(euro);
    assertEquals("EUR 1234567.35", money.with(rounding).toString());

    Locale locale = Locale.forLanguageTag("nl-NL");
    System.out.println(locale);
    System.out.println(locale.getCountry());

    MonetaryAmountFormat amountFormat1 = MonetaryFormats.getAmountFormat(locale);
    System.out.println(amountFormat1.format(money));

    AmountFormatQuery formatQuery = AmountFormatQueryBuilder.of(locale).set(CurrencyStyle.SYMBOL).build();
    MonetaryAmountFormat amountFormat2 = MonetaryFormats.getAmountFormat(formatQuery);
    System.out.println(amountFormat2.format(money));
  }
}
