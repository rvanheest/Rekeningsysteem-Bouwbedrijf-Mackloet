package com.github.rvanheest.rekeningsysteem.test.model.normal.wage;

import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
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

@RunWith(MockitoJUnitRunner.class) public class HourlyWageTest extends AbstractWageTest {

  private HourlyWage item;
  private final double hours = 10;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final MonetaryAmount wagePerHour = Money.of(4, this.currency);
  private final double wageTaxPercentage = 10;
  @Mock
  private ListItemVisitor<Object> visitor;

  @Override
  protected HourlyWage makeInstance() {
    return (HourlyWage) super.makeInstance();
  }

  @Override
  protected HourlyWage makeInstance(String description) {
    return new HourlyWage(description, this.hours, this.wagePerHour, this.wageTaxPercentage);
  }

  @Override
  protected HourlyWage makeNotInstance() {
    return (HourlyWage) super.makeNotInstance();
  }

  @Override
  protected HourlyWage makeNotInstance(String otherDescription) {
    return new HourlyWage(otherDescription, this.hours, this.wagePerHour, this.wageTaxPercentage);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
  }

  @Test
  public void testGetHours() {
    assertEquals(this.hours, this.item.getHours(), 0.0);
  }

  @Test
  public void testGetWagePerHour() {
    assertEquals(this.wagePerHour, this.item.getWagePerHour());
  }

  @Test
  public void testGetWage() {
    assertEquals(Money.of(40, this.currency), this.item.getWage());
  }

  @Test
  public void testAccept() {
    Object obj = new Object();
    when(this.visitor.visit(eq(this.item))).thenReturn(obj);

    assertEquals(obj, this.item.accept(this.visitor));

    verify(this.visitor).visit(eq(this.item));
  }

  @Test
  public void testEqualsFalseOtherUren() {
    HourlyWage loon2 = new HourlyWage(this.item.getDescription(), this.hours + 2, this.wagePerHour,
        this.wageTaxPercentage);
    assertFalse(this.item.equals(loon2));
  }

  @Test
  public void testEqualsFalseOtherUurloon() {
    HourlyWage loon2 = new HourlyWage(this.item.getDescription(), this.hours, this.wagePerHour.multiply(3),
        this.wageTaxPercentage);
    assertFalse(this.item.equals(loon2));
  }

  @Test
  public void testEqualsFalseOtherLoonBtwPercentage() {
    HourlyWage loon2 = new HourlyWage(this.item.getDescription(), this.hours, this.wagePerHour,
        this.wageTaxPercentage + 1.0);
    assertFalse(this.item.equals(loon2));
  }

  @Test
  public void testToString() {
    assertEquals("<HourlyWage[descr, 10.0, EUR 4, 10.0]>", this.item.toString());
  }
}
