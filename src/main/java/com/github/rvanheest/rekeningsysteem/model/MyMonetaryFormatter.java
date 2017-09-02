package com.github.rvanheest.rekeningsysteem.model;

import org.javamoney.moneta.format.MonetaryAmountDecimalFormatBuilder;

import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatContext;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryParseException;
import java.io.IOException;
import java.util.Locale;

public class MyMonetaryFormatter implements MonetaryAmountFormat {

  private final MonetaryAmountFormat internal;

  private MyMonetaryFormatter(Locale locale) {
    this.internal = MonetaryAmountDecimalFormatBuilder.of("#,##0.00", locale).build();
  }

  public static MyMonetaryFormatter getInstance() {
    return MyMonetaryFormatter.of(Locale.forLanguageTag("nl-NL"));
  }

  public static MyMonetaryFormatter of(Locale locale) {
    return new MyMonetaryFormatter(locale);
  }

  @Override
  public AmountFormatContext getContext() {
    return this.internal.getContext();
  }

  @Override
  public void print(Appendable appendable, MonetaryAmount amount) throws IOException {
    this.internal.print(appendable, amount);
  }

  @Override
  public MonetaryAmount parse(CharSequence text) throws MonetaryParseException {
    return this.internal.parse(text);
  }

  @Override
  public String queryFrom(MonetaryAmount amount) {
    return this.internal.queryFrom(amount);
  }
}
