package com.github.rvanheest.rekeningsysteem.test.model.normal.wage;

import com.github.rvanheest.rekeningsysteem.model.normal.wage.AbstractWage;
import com.github.rvanheest.rekeningsysteem.test.model.normal.NormalListItemTest;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;

public abstract class AbstractWageTest extends NormalListItemTest {

  private AbstractWage item;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");

  @Override
  protected AbstractWage makeInstance() {
    return (AbstractWage) super.makeInstance();
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
  }

  @Test
  public void testGetMaterialCosts() {
    assertEquals(Money.zero(this.currency), this.item.getMaterialCosts());
  }

  @Test
  public void testGetMaterialPercentage() {
    assertEquals(0.0, this.item.getMaterialCostsTaxPercentage(), 0.0);
  }

  @Test
  public void testGetMaterialCostsTax() {
    super.testGetMaterialCostsTax();
    assertEquals(Money.zero(this.currency), this.item.getMaterialCostsTax());
  }
}
