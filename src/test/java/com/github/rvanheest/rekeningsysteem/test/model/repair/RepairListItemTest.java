package com.github.rvanheest.rekeningsysteem.test.model.repair;

import com.github.rvanheest.rekeningsysteem.exception.DifferentCurrencyException;
import com.github.rvanheest.rekeningsysteem.model.repair.RepairListItem;
import com.github.rvanheest.rekeningsysteem.model.visitor.ListItemVisitor;
import com.github.rvanheest.rekeningsysteem.test.model.document.ListItemTest;
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
public class RepairListItemTest extends ListItemTest {

  private RepairListItem item;
  private final String description = "descr";
  private final String itemId = "id";
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final MonetaryAmount wage = Money.of(1, currency);
  private final MonetaryAmount materialCosts = Money.of(12, currency);
  @Mock private ListItemVisitor<Object> visitor;

  @Override
  protected RepairListItem makeInstance() throws DifferentCurrencyException {
    return new RepairListItem(this.description, this.itemId, this.wage, this.materialCosts);
  }

  @Override
  protected RepairListItem makeNotInstance() throws DifferentCurrencyException {
    return new RepairListItem(this.description + ".", this.itemId, this.wage, this.materialCosts);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
  }

  @Test(expected = DifferentCurrencyException.class)
  public void testConstructorWithDifferentCurrencies() throws DifferentCurrencyException {
    CurrencyUnit currency1 = Monetary.getCurrency("EUR");
    CurrencyUnit currency2 = Monetary.getCurrency("USD");

    new RepairListItem("", "", Money.of(1, currency1), Money.of(2, currency2));
  }

  @Test
  public void testGetDescription() {
    assertEquals(this.description, this.item.getDescription());
  }

  @Test
  public void testGetItemId() {
    assertEquals(this.itemId, this.item.getItemId());
  }

  @Test
  public void testGetWage() {
    assertEquals(this.wage, this.item.getWage());
  }

  @Test
  public void testGetMaterialCosts() {
    assertEquals(this.materialCosts, this.item.getMaterialCosts());
  }

  @Test
  public void testGetTotal() {
    assertEquals(this.wage.add(this.materialCosts), this.item.getTotal());
  }

  @Test
  public void testAccept() {
    Object obj = new Object();
    when(this.visitor.visit(eq(this.item))).thenReturn(obj);

    assertEquals(obj, this.item.accept(this.visitor));

    verify(this.visitor).visit(eq(this.item));
  }

  @Test
  public void testEqualsFalseOtherDescription() throws DifferentCurrencyException {
    RepairListItem mb = new RepairListItem(this.description + ".", this.itemId, this.wage, this.materialCosts);
    assertFalse(this.item.equals(mb));
  }

  @Test
  public void testEqualsFalseOtherItemId() throws DifferentCurrencyException {
    RepairListItem mb = new RepairListItem(this.description, this.itemId + ".", this.wage, this.materialCosts);
    assertFalse(this.item.equals(mb));
  }

  @Test
  public void testEqualsFalseOtherWage() throws DifferentCurrencyException {
    RepairListItem mb = new RepairListItem(this.description, this.itemId,
        this.wage.add(Money.of(1, this.currency)), this.materialCosts);
    assertFalse(this.item.equals(mb));
  }

  @Test
  public void testEqualsFalseOtherMaterialCosts() throws DifferentCurrencyException {
    RepairListItem mb = new RepairListItem(this.description, this.itemId, this.wage,
        this.materialCosts.add(Money.of(1, this.currency)));
    assertFalse(this.item.equals(mb));
  }

  @Test
  public void testToString() {
    assertEquals("<RepairListItem[descr, id, EUR 1, EUR 12]>", this.item.toString());
  }
}
