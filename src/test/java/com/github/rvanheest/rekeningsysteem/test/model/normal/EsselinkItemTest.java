package com.github.rvanheest.rekeningsysteem.test.model.normal;

import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.test.model.EqualsHashCodeTest;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class EsselinkItemTest extends EqualsHashCodeTest {

  private EsselinkItem artikel;
  private final String itemId = "id";
  private final String description = "descr";
  private final int amountPer = 1;
  private final String unit = "unit";
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final MonetaryAmount pricePerUnit = Money.of(3, this.currency);

  @Override
  protected Object makeInstance() {
    return new EsselinkItem(this.itemId, this.description, this.amountPer, this.unit, this.pricePerUnit);
  }

  @Override
  protected Object makeNotInstance() {
    return new EsselinkItem(this.itemId + ".", this.description, this.amountPer, this.unit, this.pricePerUnit);
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    this.artikel = new EsselinkItem(this.itemId, this.description, this.amountPer, this.unit, this.pricePerUnit);
  }

  @Test
  public void testGetItemId() {
    assertEquals(this.itemId, this.artikel.getItemId());
  }

  @Test
  public void testGetDescription() {
    assertEquals(this.description, this.artikel.getDescription());
  }

  @Test
  public void testGetAmountPer() {
    assertEquals(this.amountPer, this.artikel.getAmountPer());
  }

  @Test
  public void testGetUnit() {
    assertEquals(this.unit, this.artikel.getUnit());
  }

  @Test
  public void testGetPricePerUnit() {
    assertEquals(this.pricePerUnit, this.artikel.getPricePerUnit());
  }

  @Test
  public void testEqualsFalseOtherItemId() {
    EsselinkItem ea2 = new EsselinkItem(this.itemId + ".", this.description, this.amountPer, this.unit,
        this.pricePerUnit);
    assertFalse(this.artikel.equals(ea2));
  }

  @Test
  public void testEqualsFalseOtherDescription() {
    EsselinkItem ea2 = new EsselinkItem(this.itemId, this.description + ".", this.amountPer, this.unit,
        this.pricePerUnit);
    assertFalse(this.artikel.equals(ea2));
  }

  @Test
  public void testEqualsFalseOtherPrijsPer() {
    EsselinkItem ea2 = new EsselinkItem(this.itemId, this.description, this.amountPer + 1, this.unit,
        this.pricePerUnit);
    assertFalse(this.artikel.equals(ea2));
  }

  @Test
  public void testEqualsFalseOtherEenheid() {
    EsselinkItem ea2 = new EsselinkItem(this.itemId, this.description, this.amountPer, this.unit + ".",
        this.pricePerUnit);
    assertFalse(this.artikel.equals(ea2));
  }

  @Test
  public void testEqualsFalseOtherPrijs() {
    EsselinkItem ea2 = new EsselinkItem(this.itemId, this.description, this.amountPer, this.unit,
        this.pricePerUnit.add(Money.of(1, this.currency)));
    assertFalse(this.artikel.equals(ea2));
  }

  @Test
  public void testToString() {
    assertEquals("<EsselinkItem[id, descr, 1, unit, EUR 3]>", this.artikel.toString());
  }
}
