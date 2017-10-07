package com.github.rvanheest.rekeningsysteem.test.model.normal.wage;

import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
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
public class DefaultWageTest extends AbstractWageTest {

  private DefaultWage item;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final MonetaryAmount wage = Money.of(12, this.currency);
  private final double wageTaxPercentage = 10;
  @Mock private ListItemVisitor<Object> visitor;

  @Override
  protected DefaultWage makeInstance() {
    return (DefaultWage) super.makeInstance();
  }

  @Override
  protected DefaultWage makeInstance(String description) {
    return new DefaultWage(description, this.wage, this.wageTaxPercentage);
  }

  @Override
  protected DefaultWage makeNotInstance() {
    return (DefaultWage) super.makeNotInstance();
  }

  @Override
  protected NormalListItem makeNotInstance(String otherDescription) {
    return new DefaultWage(otherDescription, this.wage, this.wageTaxPercentage);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
  }

  @Test
  public void testGetWage() {
    assertEquals(this.wage, this.item.getWage());
  }

  @Test
  public void testGetWageTaxPercentage() {
    assertEquals(this.wageTaxPercentage, this.item.getWageTaxPercentage(), 0.0);
  }

  @Test
  public void testAccept() {
    Object obj = new Object();
    when(this.visitor.visit(eq(this.item))).thenReturn(obj);
    assertEquals(obj, this.item.accept(this.visitor));

    verify(this.visitor).visit(eq(this.item));
  }

  @Test
  public void testEqualsFalseOtherWage() {
    DefaultWage loon2 = new DefaultWage(this.item.getDescription(), this.wage.multiply(2),
        this.item.getWageTaxPercentage());
    assertFalse(this.item.equals(loon2));
  }

  @Test
  public void testEqualsFalseOtherWageTaxPercentage() {
    DefaultWage loon2 = new DefaultWage(this.item.getDescription(), this.wage, this.wageTaxPercentage + 1.0);
    assertFalse(this.item.equals(loon2));
  }

  @Test
  public void testToString() {
    assertEquals("<DefaultWage[descr, EUR 12, 10.0]>", this.item.toString());
  }
}
