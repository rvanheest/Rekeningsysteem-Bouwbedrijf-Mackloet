package com.github.rvanheest.rekeningsysteem.test.model.totals;

import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import com.github.rvanheest.rekeningsysteem.test.model.EqualsHashCodeTest;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TotalsTest extends EqualsHashCodeTest {

  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private Totals totals;

  @Override
  protected Totals makeInstance() {
    return new Totals(currency, 50.0, Money.of(2, currency), Money.of(16, currency));
  }

  @Override
  protected Totals makeNotInstance() {
    return new Totals(currency, 0.0, Money.of(2, currency), Money.of(16, currency));
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    this.totals = this.makeInstance();
  }

  @Test
  public void testAdd() {
    this.totals = this.totals.add(50, Money.of(0, currency), Money.of(20, currency))
        .add(50, Money.of(0, currency), Money.of(30, currency))
        .add(10, Money.of(0, currency), Money.of(100, currency))
        .add(20, Money.of(0, currency), Money.of(100, currency));

    Map<Double, MonetaryAmount> expected = new HashMap<>();
    expected.put(50.0, Money.of(66, currency));
    expected.put(10.0, Money.of(100, currency));
    expected.put(20.0, Money.of(100, currency));

    assertEquals(expected, this.totals.getTax());
    assertEquals(Money.of(2, currency), this.totals.getSubtotal());
    assertEquals(Money.of(268, currency), this.totals.getTotal());
  }

  @Test
  public void testGetNetto() {
    Map<Double, MonetaryAmount> expected = new HashMap<>();
    expected.put(50.0, Money.of(2, currency));
    assertEquals(expected, this.totals.getNet());
  }

  @Test
  public void testGetBtw() {
    Map<Double, MonetaryAmount> expected = new HashMap<>();
    expected.put(50.0, Money.of(16, currency));
    assertEquals(expected, this.totals.getTax());
  }

  @Test
  public void testGetSubtotals() {
    assertEquals(Money.of(2, currency), this.totals.getSubtotal());
  }

  @Test
  public void testGetTotals() {
    assertEquals(Money.of(18, currency), this.totals.getTotal());
  }

  @Test
  public void testPlus() {
    Totals t2 = new Totals(this.currency, 20, Money.of(3, currency), Money.of(100, currency));

    Totals expected = new Totals(this.currency).add(50, Money.of(2, currency), Money.of(16, currency))
        .add(20, Money.of(3, currency), Money.of(100, currency));

    assertEquals(expected, this.totals.add(t2));
  }

  @Test
  public void testEqualsFalseOtherNetto() {
    assertFalse(this.totals.equals(new Totals(this.currency, 50, Money.of(3, currency), Money.of(16, currency))));
  }

  @Test
  public void testEqualsFalseOtherBtwPercentage() {
    assertFalse(this.totals.equals(new Totals(this.currency, 40, Money.of(2, currency), Money.of(16, currency))));
  }

  @Test
  public void testEqualsFalseOtherBtw() {
    assertFalse(this.totals.equals(new Totals(this.currency, 50, Money.of(2, currency), Money.of(15, currency))));
  }

  @Test
  public void testToString() {
    assertEquals("<Totals[EUR, {50.0=(EUR 2, EUR 16)}]>", this.totals.toString());
  }
}
