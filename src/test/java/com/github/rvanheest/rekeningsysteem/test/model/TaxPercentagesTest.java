package com.github.rvanheest.rekeningsysteem.test.model;

import com.github.rvanheest.rekeningsysteem.model.TaxPercentages;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class TaxPercentagesTest extends EqualsHashCodeTest {

  private TaxPercentages taxPercentages;
  private final double wagePercentage = 6.0;
  private final double materialCostsPercentage = 21.0;

  @Override
  protected TaxPercentages makeInstance() {
    return new TaxPercentages(this.wagePercentage, this.materialCostsPercentage);
  }

  @Override
  protected TaxPercentages makeNotInstance() {
    return new TaxPercentages(this.wagePercentage + 1, this.materialCostsPercentage);
  }

  @Before
  @Override
  public void setUp() throws Exception {
    super.setUp();
    this.taxPercentages = this.makeInstance();
  }

  @Test
  public void testGetMaterialCostsPercentage() {
    assertEquals(this.materialCostsPercentage, this.taxPercentages.getMaterialCostsPercentage(), 0.0);
  }

  @Test
  public void testGetWagePercentage() {
    assertEquals(this.wagePercentage, this.taxPercentages.getWagePercentage(), 0.0);
  }

  @Test
  public void testEqualsFalseOtherMaterialCostsPercentage() {
    TaxPercentages btw = new TaxPercentages(this.wagePercentage, this.materialCostsPercentage + 1);
    assertFalse(this.taxPercentages.equals(btw));
  }

  @Test
  public void testEqualsFalseOtherWagePercentage() {
    TaxPercentages btw = new TaxPercentages(this.wagePercentage + 1, this.materialCostsPercentage);
    assertFalse(this.taxPercentages.equals(btw));
  }

  @Test
  public void testToString() {
    assertEquals("<TaxPercentages[6.0, 21.0]>", this.taxPercentages.toString());
  }
}
