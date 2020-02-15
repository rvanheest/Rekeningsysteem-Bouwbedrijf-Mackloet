package org.rekeningsysteem.test.io.xml.adaptee.util;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adaptee.util.BtwPercentageAdaptee;

import static org.junit.Assert.assertEquals;

public class BtwPercentageAdapteeTest {

  private final double percentage = 0.2;
  private final boolean verlegd = true;
  private BtwPercentageAdaptee adaptee;
  
  @Before
  public void setUp() {
    this.adaptee = BtwPercentageAdaptee.build(adaptee -> adaptee
        .withBtwPercentage(percentage)
        .withVerlegd(verlegd));
  }
  
  @Test
  public void testSetGetPercentage() {
    assertEquals(this.percentage, this.adaptee.getBtwPercentage(), 0.0);
  }
  
  @Test
  public void testSetIsVerlegd() {
    assertEquals(this.verlegd, this.adaptee.isVerlegd());
  }
}
