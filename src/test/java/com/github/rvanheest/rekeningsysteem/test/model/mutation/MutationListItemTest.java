package com.github.rvanheest.rekeningsysteem.test.model.mutation;

import com.github.rvanheest.rekeningsysteem.model.mutation.MutationListItem;
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
public class MutationListItemTest extends ListItemTest {

  private MutationListItem item;
  private final String description = "descr";
  private final String itemId = "id";
  private final CurrencyUnit currency = Monetary.getCurrency("EUR");
  private final MonetaryAmount amount = Money.of(10, this.currency);
  @Mock private ListItemVisitor<Object> visitor;

  @Override
  protected MutationListItem makeInstance() {
    return new MutationListItem(this.description, this.itemId, this.amount);
  }

  @Override
  protected MutationListItem makeNotInstance() {
    return new MutationListItem(this.description + ".", this.itemId, this.amount);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
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
    assertEquals(Money.zero(this.currency), this.item.getWage());
  }

  @Test
  public void testGetMaterialCosts() {
    assertEquals(this.amount, this.item.getMaterialCosts());
  }

  @Test
  public void testGetTotal() {
    assertEquals(this.amount, this.item.getTotal());
  }

  @Test
  public void testAccept() {
    Object obj = new Object();
    when(this.visitor.visit(eq(this.item))).thenReturn(obj);

    assertEquals(obj, this.item.accept(this.visitor));

    verify(this.visitor).visit(eq(this.item));
  }

  @Test
  public void testEqualsFalseOtherDescription() {
    MutationListItem mb = new MutationListItem(this.description + ".", this.itemId, this.amount);
    assertFalse(this.item.equals(mb));
  }

  @Test
  public void testEqualsFalseOtherItemId() {
    MutationListItem mb = new MutationListItem(this.description, this.itemId + ".", this.amount);
    assertFalse(this.item.equals(mb));
  }

  @Test
  public void testEqualsFalseOtherAmount() {
    MutationListItem mb = new MutationListItem(this.description, this.itemId, this.amount.multiply(2));
    assertFalse(this.item.equals(mb));
  }

  @Test
  public void testToString() {
    assertEquals("<MutationListItem[descr, id, EUR 10]>", this.item.toString());
  }
}
