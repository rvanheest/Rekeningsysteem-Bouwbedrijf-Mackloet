package com.github.rvanheest.rekeningsysteem.test;

import com.github.rvanheest.rekeningsysteem.model.MyMonetaryFormatter;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.format.CurrencyStyle;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryOperator;
import javax.money.format.AmountFormatQuery;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Currency;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class MoneyDemo {

  @Test
  public void testMoney() {
    CurrencyUnit euro = Monetary.getCurrency("EUR");
    MonetaryAmount money = Money.of(1234567.346, euro);
    MonetaryOperator rounding = Monetary.getRounding(euro);
    assertEquals("EUR 1234567.35", money.with(rounding).toString());

    Locale locale = Locale.forLanguageTag("nl-NL");
    System.out.println(locale);
    System.out.println(locale.getCountry());

    MonetaryAmountFormat amountFormat1 = MonetaryFormats.getAmountFormat(locale);
    assertEquals("EUR 1.234.567,35", amountFormat1.format(money));
    System.out.println(amountFormat1.format(money));

    AmountFormatQuery formatQuery1 = AmountFormatQueryBuilder.of(locale).set(CurrencyStyle.SYMBOL).build();
    MonetaryAmountFormat amountFormat2 = MonetaryFormats.getAmountFormat(formatQuery1);
    assertEquals("€ 1.234.567,35", amountFormat2.format(money));
    System.out.println(amountFormat2.format(money));

    MonetaryAmountFormat amountFormat3 = MyMonetaryFormatter.getInstance();
    System.out.println(amountFormat3.format(money));
    System.out.println(amountFormat3.format(Money.of(0.129, euro)));
    System.out.println(amountFormat3.format(Money.zero(euro)));
    System.out.println(amountFormat3.format(Money.of(-0.238, euro)));
    System.out.println(amountFormat3.format(Money.of(-3.1415, euro)));

    System.out.println(Currency.getInstance(euro.getCurrencyCode()).getSymbol(locale));

    MonetaryAmount parsed = amountFormat3.parse("-0,13");
    System.out.println(parsed);
    MonetaryAmount dollars = parsed.with(m -> Money.of(m.getNumber(), Monetary.getCurrency("EUR")));
    System.out.println(dollars);

    System.out.println(amountFormat2.parse("€ 12,13"));
  }
}
