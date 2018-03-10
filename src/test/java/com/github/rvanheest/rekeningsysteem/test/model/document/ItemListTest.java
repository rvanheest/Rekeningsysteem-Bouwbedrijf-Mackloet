package com.github.rvanheest.rekeningsysteem.test.model.document;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.document.ItemList;
import com.github.rvanheest.rekeningsysteem.model.document.ListItem;
import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.SimpleListItem;
import com.github.rvanheest.rekeningsysteem.model.normal.wage.DefaultWage;
import com.github.rvanheest.rekeningsysteem.model.totals.Totals;
import com.github.rvanheest.rekeningsysteem.model.totals.TotalsListItemVisitor;
import com.github.rvanheest.rekeningsysteem.test.TestSupportFixture;
import com.github.rvanheest.rekeningsysteem.test.model.EqualsHashCodeTest;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemListTest extends EqualsHashCodeTest {

  private ItemList<ListItem> list;

  @Mock private TotalsListItemVisitor visitor;
  private CurrencyUnit currency = Monetary.getCurrency("EUR");
  private MutationListItem item1 = new MutationListItem("", "", Money.of(1, currency));
  private SimpleListItem item2 = new SimpleListItem("", Money.of(2, currency), 0.0);
  private DefaultWage item3 = new DefaultWage("", Money.of(4, currency), 50.0);
  private SimpleListItem item4 = new SimpleListItem("", Money.of(5, currency), 25.0);

  @Override
  protected ItemList<ListItem> makeInstance() {
    return new ItemList<>(this.currency, this.visitor);
  }

  @Override
  protected ItemList<ListItem> makeNotInstance() throws DifferentCurrencyException {
    return new ItemList<>(this.currency, this.visitor, Collections.singletonList(this.item1));
  }

  @BeforeClass
  public static void setUpClass() {
    TestSupportFixture.slfBridger();
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    this.list = this.makeInstance();
  }

  @Test
  public void testAddItemsWithSameCurrencyAndGetList() throws DifferentCurrencyException {
    this.list.add(this.item1);
    this.list.add(this.item2);
    this.list.add(this.item3);
    this.list.add(this.item4);
    assertEquals(Arrays.asList(this.item1, this.item2, this.item3, this.item4), this.list.getList());
  }

  @Test(expected = DifferentCurrencyException.class)
  public void testAddItemsWithOtherCurrency() throws DifferentCurrencyException {
    this.list.add(new MutationListItem("", "", Money.of(10, Monetary.getCurrency("USD"))));
  }

  @Test
  public void testRemoveItem() throws DifferentCurrencyException {
    this.testAddItemsWithSameCurrencyAndGetList();

    this.list.remove(this.item1);
    assertEquals(Arrays.asList(this.item2, this.item3, this.item4), this.list.getList());
  }

  @Test
  public void testRemoveItemThatIsNotInTheList() throws DifferentCurrencyException {
    this.testRemoveItem();

    this.list.remove(this.item1);
    // nothing changed, item1 was already removed
    assertEquals(Arrays.asList(this.item2, this.item3, this.item4), this.list.getList());
  }

  @Test
  public void testClearList() throws DifferentCurrencyException {
    this.testAddItemsWithSameCurrencyAndGetList();

    this.list.clear();
    assertTrue(this.list.getList().isEmpty());
  }

  @Test
  public void testClearAnEmptyList() throws DifferentCurrencyException {
    this.testClearList();

    this.list.clear();
    // nothing changed, the list was already cleared
    assertTrue(this.list.getList().isEmpty());
  }

  @Test
  public void testAddAllWithBothEmptyAndSameCurrency() throws DifferentCurrencyException {
    ItemList<ListItem> list1 = new ItemList<>(this.currency);
    ItemList<ListItem> list2 = new ItemList<>(this.currency);

    ItemList<ListItem> result = ItemList.merge(list1, list2);
    assertTrue(result.getList().isEmpty());
  }

  @Test
  public void testAddAllWithLeftEmptyAndSameCurrency() throws DifferentCurrencyException {
    ItemList<ListItem> list1 = new ItemList<>(this.currency);

    ItemList<ListItem> list2 = new ItemList<>(this.currency);
    list2.add(this.item3);
    list2.add(this.item4);

    ItemList<ListItem> result = ItemList.merge(list1, list2);
    assertEquals(2, result.getList().size());
    assertTrue(result.getList().containsAll(Arrays.asList(this.item3, this.item4)));
  }

  @Test
  public void testAddAllWithRightEmptyAndSameCurrency() throws DifferentCurrencyException {
    ItemList<ListItem> list1 = new ItemList<>(this.currency);
    list1.add(this.item1);
    list1.add(this.item2);

    ItemList<ListItem> list2 = new ItemList<>(this.currency);

    ItemList<ListItem> result = ItemList.merge(list1, list2);
    assertEquals(2, result.getList().size());
    assertTrue(result.getList().containsAll(Arrays.asList(this.item1, this.item2)));
  }

  @Test
  public void testAddAllWithSameCurrency() throws DifferentCurrencyException {
    ItemList<ListItem> list1 = new ItemList<>(this.currency);
    list1.add(this.item1);
    list1.add(this.item2);

    ItemList<ListItem> list2 = new ItemList<>(this.currency);
    list2.add(this.item3);
    list2.add(this.item4);

    ItemList<ListItem> result = ItemList.merge(list1, list2);
    assertEquals(4, result.getList().size());
    assertTrue(result.getList().containsAll(Arrays.asList(this.item1, this.item2, this.item3, this.item4)));
  }

  @Test(expected = DifferentCurrencyException.class)
  public void testAddAllWithDifferentCurrency() throws DifferentCurrencyException {
    ItemList<ListItem> list1 = new ItemList<>(Monetary.getCurrency("EUR"));
    ItemList<ListItem> list2 = new ItemList<>(Monetary.getCurrency("USD"));

    ItemList.merge(list1, list2);
  }

  @Test
  public void testGetTotalenEmptyList() {
    assertEquals(new Totals(currency), new ItemList<>(currency).getTotals());

    verifyZeroInteractions(this.visitor);
  }

  @Test
  public void testGetTotalenNonEmptyList() throws DifferentCurrencyException {
    when(this.visitor.visit(eq(this.item1))).thenReturn(t -> t.add(this.item1.getMaterialCosts()));
    when(this.visitor.visit(eq(this.item2))).thenReturn(t -> t.add(this.item2.getMaterialCostsTaxPercentage(),
        this.item2.getMaterialCosts(), this.item2.getMaterialCostsTax()));
    when(this.visitor.visit(eq(this.item3))).thenReturn(t -> t.add(this.item3.getWageTaxPercentage(),
        this.item3.getWage(), this.item3.getWageTax()));
    when(this.visitor.visit(eq(this.item4))).thenReturn(t -> t.add(this.item4.getMaterialCostsTaxPercentage(),
        this.item4.getMaterialCosts(), this.item4.getMaterialCostsTax()));

    for (int i = 0; i < 4; i++ ) {
      this.list.add(this.item1);
      this.list.add(this.item2);
      this.list.add(this.item3);
      this.list.add(this.item4);
    }

    Totals result = this.list.getTotals();

    // visitor methods are visited 4 times each because the items are all added 4 times
    verify(this.visitor, times(4)).visit(eq(this.item1));
    verify(this.visitor, times(4)).visit(eq(this.item2));
    verify(this.visitor, times(4)).visit(eq(this.item3));
    verify(this.visitor, times(4)).visit(eq(this.item4));

    Map<Double, MonetaryAmount> expectedBtw = new HashMap<>();
    expectedBtw.put(50.0, Money.of(8, currency));
    expectedBtw.put(25.0, Money.of(5, currency));
    expectedBtw.put(0.0, Money.of(0, currency));

    assertEquals(Money.of(12, currency), result.getNet().get(0.0));
    assertEquals(Money.of(20, currency), result.getNet().get(25.0));
    assertEquals(Money.of(16, currency), result.getNet().get(50.0));
    assertEquals(Money.of(8, currency), result.getTax().get(50.0));
    assertEquals(Money.of(5, currency), result.getTax().get(25.0));
    assertEquals(expectedBtw, result.getTax());
    assertEquals(Money.of(48, currency), result.getSubtotal());
    assertEquals(Money.of(61, currency), result.getTotal());

    verifyNoMoreInteractions(this.visitor);
  }

  @Test
  public void testGetTotalenNonEmptyListWithZeros() throws DifferentCurrencyException {
    DefaultWage loon = new DefaultWage("", Money.zero(currency), 21.0);
    SimpleListItem materiaal = new SimpleListItem("", Money.of(2, currency), 50.0);

    when(this.visitor.visit(eq(materiaal)))
        .thenReturn(t -> t.add(materiaal.getMaterialCostsTaxPercentage(),
            materiaal.getMaterialCosts(), materiaal.getMaterialCostsTax()));
    when(this.visitor.visit(eq(loon)))
        .thenReturn(t -> t.add(loon.getWageTaxPercentage(),
            loon.getWage(), loon.getWageTax()));

    this.list.add(loon);
    this.list.add(materiaal);

    Totals result = this.list.getTotals();

    verify(this.visitor).visit(eq(materiaal));
    verify(this.visitor).visit(eq(loon));

    Map<Double, MonetaryAmount> expectedBtw = new HashMap<>();
    expectedBtw.put(50.0, Money.of(1, currency));
    expectedBtw.put(21.0, Money.of(0.0, currency));

    assertEquals(Money.of(2, currency), result.getNet().get(50.0));
    assertEquals(Money.of(1, currency), result.getTax().get(50.0));
    assertEquals(expectedBtw, result.getTax());
    assertEquals(Money.of(2, currency), result.getSubtotal());
    assertEquals(Money.of(3, currency), result.getTotal());

    verifyNoMoreInteractions(this.visitor);
  }
}
