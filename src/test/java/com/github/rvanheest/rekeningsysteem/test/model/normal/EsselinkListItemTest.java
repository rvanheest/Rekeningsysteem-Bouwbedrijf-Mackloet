package com.github.rvanheest.rekeningsysteem.test.model.normal;

import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EsselinkListItemTest extends NormalListItemTest {

  private EsselinkListItem item;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final EsselinkItem esselinkItem = new EsselinkItem("id", "descr", 2, "unit", Money.of(1, this.currency));
  private final double amount = 5;
  private final double taxPercentage = 10;
  @Mock
  private ListItemVisitor<Object> visitor;

  @Override
  protected EsselinkListItem makeInstance() {
    return (EsselinkListItem) super.makeInstance();
  }

  @Override
  protected EsselinkListItem makeInstance(String description) {
    return new EsselinkListItem(description, this.esselinkItem, this.amount, this.taxPercentage);
  }

  @Override
  protected EsselinkListItem makeNotInstance() {
    return (EsselinkListItem) super.makeNotInstance();
  }

  @Override
  protected NormalListItem makeNotInstance(String otherDescription) {
    return new EsselinkListItem(otherDescription, this.esselinkItem, this.amount, this.taxPercentage);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
  }

  @Test
  public void testGetItem() {
    assertEquals(this.esselinkItem, this.item.getItem());
  }

  @Test
  public void testGetAmount() {
    assertEquals(this.amount, this.item.getAmount(), 0.0);
  }

  @Test
  public void testGetMaterialCosts() {
    assertEquals(Money.of(2.5, this.currency), this.item.getMaterialCosts());
  }

  @Test
  public void testGetMaterialTaxPercentage() {
    assertEquals(this.taxPercentage, this.item.getMaterialCostsTaxPercentage(), 0.0);
  }

  @Test
  public void testAccept() {
    Object obj = new Object();
    when(this.visitor.visit(this.item)).thenReturn(obj);

    assertEquals(obj, this.item.accept(this.visitor));

    verify(this.visitor).visit(eq(this.item));
  }

  @Test
  public void testEqualsFalseOtherEsselinkItem() {
    EsselinkItem ea = new EsselinkItem("", "", 1, "", Money.of(2, this.currency));
    EsselinkListItem gea = new EsselinkListItem(ea, this.amount, this.taxPercentage);
    assertFalse(this.item.equals(gea));
  }

  @Test
  public void testEqualsFalseOtherAantal() {
    EsselinkListItem gea = new EsselinkListItem(this.esselinkItem, this.amount + 1, this.taxPercentage);
    assertFalse(this.item.equals(gea));
  }

  @Test
  public void testEqualsFalseOtherMateriaalBtwPercentage() {
    EsselinkListItem gea = new EsselinkListItem(this.esselinkItem, this.amount, this.taxPercentage + 1);
    assertFalse(this.item.equals(gea));
  }

  @Test
  public void testToString() {
    assertEquals("<EsselinkListItem[descr, 10.0, 5.0, <EsselinkItem[id, descr, 2, unit, EUR 1]>]>",
        this.item.toString());
  }
}
