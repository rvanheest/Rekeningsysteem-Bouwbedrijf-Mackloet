package com.github.rvanheest.rekeningsysteem.test.pdf;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkItem;
import com.github.rvanheest.rekeningsysteem.model.normal.EsselinkListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.HourlyWage;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.pdf.PdfListItemVisitor;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PdfListItemVisitorTest {

  private PdfListItemVisitor visitor;
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  @Mock private MonetaryAmountFormat formatter;

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() {
    this.visitor = new PdfListItemVisitor(this.formatter);
  }

  @Test
  public void testVisitMutationListItem() {
    Money m = Money.of(12.34, this.currency);
    when(this.formatter.format(any(MonetaryAmount.class))).thenReturn("output");
    MutationListItem item = new MutationListItem("descr", "bonn", m);
    List<String> expected = Arrays.asList("descr", "bonn", "output");

    assertEquals(expected, this.visitor.visit(item));
    verify(this.formatter).format(eq(m));
    verifyNoMoreInteractions(this.formatter);
  }

  @Test
  public void testVisitMutationListItemZero() {
    MutationListItem item = new MutationListItem("descr", "bonn", Money.zero(this.currency));

    assertTrue(this.visitor.visit(item).isEmpty());
    verifyZeroInteractions(this.formatter);
  }

  @Test
  public void testVisitRepairListItem() throws DifferentCurrencyException {
    Money m1 = Money.of(12.34, this.currency);
    Money m2 = Money.of(56.78, this.currency);
    when(this.formatter.format(any(MonetaryAmount.class))).thenReturn("output1", "output2", "output3");
    RepairListItem item = new RepairListItem("descr", "bonn", m1, m2);
    List<String> expected = Arrays.asList("descr", "bonn", "output1", "output2", "output3");

    assertEquals(expected, this.visitor.visit(item));
    verify(this.formatter).format(eq(m1));
    verify(this.formatter).format(eq(m2));
    verify(this.formatter).format(eq(m1.add(m2)));
    verifyNoMoreInteractions(this.formatter);
  }

  @Test
  public void testVisitRepairListItemZero() throws DifferentCurrencyException {
    RepairListItem item = new RepairListItem("descr", "bonn", Money.zero(this.currency), Money.zero(this.currency));

    assertTrue(this.visitor.visit(item).isEmpty());
    verifyZeroInteractions(this.formatter);
  }

  @Test
  public void testVisitSimpleListItem() {
    Money m = Money.of(12.34, this.currency);
    when(this.formatter.format(any(MonetaryAmount.class))).thenReturn("output");
    SimpleListItem item = new SimpleListItem("descr", m, 19.5);
    List<String> expected = Arrays.asList("descr", "output", "19.5");

    assertEquals(expected, this.visitor.visit(item));
    verify(this.formatter).format(eq(m));
    verifyNoMoreInteractions(this.formatter);
  }

  @Test
  public void testVisitSimpleListItemZero() {
    SimpleListItem item = new SimpleListItem("descr", Money.zero(this.currency), 20.0);

    assertTrue(this.visitor.visit(item).isEmpty());
    verifyZeroInteractions(this.formatter);
  }

  @Test
  public void testVisitEsselinkListItemIntegerAmount() {
    Money m = Money.of(56.78, this.currency);
    when(this.formatter.format(any(MonetaryAmount.class))).thenReturn("output2");
    EsselinkItem ei = new EsselinkItem("itemId", "descr", 10, "units", m);
    EsselinkListItem item = new EsselinkListItem(ei, 20, 30);
    List<String> expected = Arrays.asList("20x descr", "output2", "30.0");

    assertEquals(expected, this.visitor.visit(item));
    verify(this.formatter).format(eq(m.multiply(2)));
    verifyNoMoreInteractions(this.formatter);
  }

  @Test
  public void testVisitEsselinkListItemDoubleAmount() {
    Money m = Money.of(56.78, this.currency);
    when(this.formatter.format(any(MonetaryAmount.class))).thenReturn("output2");
    EsselinkItem ei = new EsselinkItem("itemId", "descr", 10, "units", m);
    EsselinkListItem item = new EsselinkListItem(ei, 20.4, 30);
    List<String> expected = Arrays.asList("20.4x descr", "output2", "30.0");

    assertEquals(expected, this.visitor.visit(item));
    verify(this.formatter).format(eq(m.multiply(2.04)));
    verifyNoMoreInteractions(this.formatter);
  }

  @Test
  public void testVisitEsselinkListItemZero() {
    EsselinkItem ei = new EsselinkItem("itemId", "descr", 10, "units", Money.zero(this.currency));
    EsselinkListItem item = new EsselinkListItem(ei, 20, 30);

    assertTrue(this.visitor.visit(item).isEmpty());
    verifyZeroInteractions(this.formatter);
  }

  @Test
  public void testVisitDefaultWage() {
    Money m = Money.of(12.34, this.currency);
    when(this.formatter.format(any(MonetaryAmount.class))).thenReturn("output");
    DefaultWage item = new DefaultWage("descr", m, 19.5);
    List<String> expected = Arrays.asList("descr", "output", "19.5");

    assertEquals(expected, this.visitor.visit(item));
    verify(this.formatter).format(eq(m));
    verifyNoMoreInteractions(this.formatter);
  }

  @Test
  public void testVisitDefaultWageZero() {
    DefaultWage item = new DefaultWage("descr", Money.zero(this.currency), 20.0);

    assertTrue(this.visitor.visit(item).isEmpty());
    verifyZeroInteractions(this.formatter);
  }

  @Test
  public void testVisitHourlyWage() {
    Money m = Money.of(12.34, this.currency);
    when(this.formatter.format(any(MonetaryAmount.class))).thenReturn("output1", "output2");
    HourlyWage item = new HourlyWage("descr", 3.5, m, 19.5);
    List<String> expected = Arrays.asList("3.5 uren à output1", "output2", "19.5");

    assertEquals(expected, this.visitor.visit(item));
    verify(this.formatter).format(eq(m));
    verify(this.formatter).format(eq(m.multiply(3.5)));
    verifyNoMoreInteractions(this.formatter);
  }

  @Test
  public void testVisitHourlyWageZero() {
    HourlyWage item = new HourlyWage("descr", 3.5, Money.zero(this.currency), 20.0);

    assertTrue(this.visitor.visit(item).isEmpty());
    verifyZeroInteractions(this.formatter);
  }
}
