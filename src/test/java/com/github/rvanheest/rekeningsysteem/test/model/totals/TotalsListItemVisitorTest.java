package com.github.rvanheest.rekeningsysteem.test.model.totals;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import com.github.rvanheest.rekeningsysteem.model.totals.TotalsListItemVisitor;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;

public class TotalsListItemVisitorTest {

  private TotalsListItemVisitor visitor;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final Totals input = new Totals(currency, 20.0, Money.of(10, this.currency), Money.of(2, this.currency));

  @Before
  public void setUp() {
    this.visitor = new TotalsListItemVisitor();
  }

  @Test
  public void testVisitMutationListItem() {
    MutationListItem item = new MutationListItem("", "", Money.of(5, this.currency));
    Totals newTotals = visitor.visit(item).apply(this.input);

    assertEquals(Money.of(10, this.currency), newTotals.getNet().get(20.0));
    assertEquals(Money.of(5, this.currency), newTotals.getNet().get(0.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(20.0));
    assertEquals(Money.zero(this.currency), newTotals.getTax().get(0.0));
    assertEquals(Money.of(15, this.currency), newTotals.getSubtotal());
    assertEquals(Money.of(17, this.currency), newTotals.getTotal());
  }

  @Test
  public void testVisitRepairListItem() throws DifferentCurrencyException {
    RepairListItem item = new RepairListItem("", "", Money.of(7, this.currency), Money.of(5, this.currency));
    Totals newTotals = visitor.visit(item).apply(this.input);

    assertEquals(Money.of(10, this.currency), newTotals.getNet().get(20.0));
    assertEquals(Money.of(12, this.currency), newTotals.getNet().get(0.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(20.0));
    assertEquals(Money.zero(this.currency), newTotals.getTax().get(0.0));
    assertEquals(Money.of(22, this.currency), newTotals.getSubtotal());
    assertEquals(Money.of(24, this.currency), newTotals.getTotal());
  }

  @Test
  public void testVisitSimpleListItem() {
    SimpleListItem item = new SimpleListItem("", Money.of(20, this.currency), 10.0);
    Totals newTotals = visitor.visit(item).apply(this.input);

    assertEquals(Money.of(10, this.currency), newTotals.getNet().get(20.0));
    assertEquals(Money.of(20, this.currency), newTotals.getNet().get(10.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(20.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(10.0));
    assertEquals(Money.of(30, this.currency), newTotals.getSubtotal());
    assertEquals(Money.of(34, this.currency), newTotals.getTotal());
  }

  @Test
  public void testVisitEsselinkListItem() {
    EsselinkItem esselink = new EsselinkItem("", "", 2, "liter", Money.of(10, this.currency));
    EsselinkListItem item = new EsselinkListItem(esselink, 5, 10.0);
    Totals newTotals = visitor.visit(item).apply(this.input);

    assertEquals(Money.of(10, this.currency), newTotals.getNet().get(20.0));
    assertEquals(Money.of(25, this.currency), newTotals.getNet().get(10.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(20.0));
    assertEquals(Money.of(2.5, this.currency), newTotals.getTax().get(10.0));
    assertEquals(Money.of(35, this.currency), newTotals.getSubtotal());
    assertEquals(Money.of(39.5, this.currency), newTotals.getTotal());
  }

  @Test
  public void testVisitDefaultWage() {
    DefaultWage item = new DefaultWage("", Money.of(20, this.currency), 10.0);
    Totals newTotals = visitor.visit(item).apply(this.input);

    assertEquals(Money.of(10, this.currency), newTotals.getNet().get(20.0));
    assertEquals(Money.of(20, this.currency), newTotals.getNet().get(10.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(20.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(10.0));
    assertEquals(Money.of(30, this.currency), newTotals.getSubtotal());
    assertEquals(Money.of(34, this.currency), newTotals.getTotal());
  }

  @Test
  public void testVisitHourlyWage() {
    HourlyWage item = new HourlyWage("", 20, Money.of(20, this.currency), 10.0);
    Totals newTotals = visitor.visit(item).apply(this.input);

    assertEquals(Money.of(10, this.currency), newTotals.getNet().get(20.0));
    assertEquals(Money.of(400, this.currency), newTotals.getNet().get(10.0));
    assertEquals(Money.of(2, this.currency), newTotals.getTax().get(20.0));
    assertEquals(Money.of(40, this.currency), newTotals.getTax().get(10.0));
    assertEquals(Money.of(410, this.currency), newTotals.getSubtotal());
    assertEquals(Money.of(452, this.currency), newTotals.getTotal());
  }
}
