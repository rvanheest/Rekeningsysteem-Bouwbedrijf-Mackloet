package com.github.rvanheest.rekeningsysteem.test.model.normal;

import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleListItemTest extends NormalListItemTest {

  private SimpleListItem item;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final MonetaryAmount amount = Money.of(21, this.currency);
  private final double taxPercentage = 10;
  @Mock private ListItemVisitor<Object> visitor;

  @Override
  protected SimpleListItem makeInstance() {
    return (SimpleListItem) super.makeInstance();
  }

  @Override
  protected SimpleListItem makeInstance(String description) {
    return new SimpleListItem(description, this.amount, this.taxPercentage);
  }

  @Override
  protected SimpleListItem makeNotInstance() {
    return (SimpleListItem) super.makeNotInstance();
  }

  @Override
  protected SimpleListItem makeNotInstance(String otherDescription) {
    return new SimpleListItem(otherDescription, this.amount, this.taxPercentage);
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
  }

  @Test
  public void testGetMaterialCosts() {
    assertEquals(this.amount, this.item.getMaterialCosts());
  }

  @Test
  public void testGetMateriaalBtwPercentage() {
    assertEquals(this.taxPercentage, this.item.getMaterialCostsTaxPercentage(), 0.0);
  }

  @Test
  public void testAccept() {
    Object obj = new Object();
    when(this.visitor.visit(eq(this.item))).thenReturn(obj);

    assertEquals(obj, this.item.accept(this.visitor));

    verify(this.visitor).visit(eq(this.item));
  }

  @Test
  public void testEqualsFalseOtherPrijs() {
    SimpleListItem aa2 = new SimpleListItem(this.item.getDescription(), this.amount.multiply(2), this.taxPercentage);
    assertFalse(this.item.equals(aa2));
  }

  @Test
  public void testEqualsFalseOtherBtw() {
    SimpleListItem aa2 = new SimpleListItem(this.item.getDescription(), this.amount, this.taxPercentage + 1);
    assertFalse(this.item.equals(aa2));
  }

  @Test
  public void testToString() {
    assertEquals("<SimpleListItem[descr, EUR 21, 10.0]>",
        this.item.toString());
  }
}
