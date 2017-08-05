package com.github.rvanheest.rekeningsysteem.test.model.normal;

import com.github.rvanheest.rekeningsysteem.model.normal.TaxedListItem;
import com.github.rvanheest.rekeningsysteem.test.model.document.ListItemTest;
import org.junit.Before;
import org.junit.Test;

import javax.money.MonetaryAmount;

import static org.junit.Assert.assertEquals;

public abstract class TaxedListItemTest extends ListItemTest {

  private TaxedListItem item;

  @Override
  protected abstract TaxedListItem makeInstance();

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.item = this.makeInstance();
  }

  @Test
  public void testGetWageTax() {
    MonetaryAmount wage = this.item.getWage();
    double percentage = this.item.getWageTaxPercentage();

    MonetaryAmount expected = wage.multiply(percentage).divide(100);
    assertEquals(expected, this.item.getWageTax());
  }

  @Test
  public void testGetMaterialCostsTax() {
    MonetaryAmount materialCosts = this.item.getMaterialCosts();
    double percentage = this.item.getMaterialCostsTaxPercentage();

    MonetaryAmount expected = materialCosts.multiply(percentage).divide(100);
    assertEquals(expected, this.item.getMaterialCostsTax());
  }
}
