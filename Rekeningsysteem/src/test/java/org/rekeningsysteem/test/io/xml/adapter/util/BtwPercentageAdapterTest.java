package org.rekeningsysteem.test.io.xml.adapter.util;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.xml.adaptee.util.BtwPercentageAdaptee;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;

import static org.junit.Assert.assertEquals;

public class BtwPercentageAdapterTest {

  private BtwPercentageAdapter adapter;
  
  @Before
  public void setUp() {
    this.adapter = new BtwPercentageAdapter();
  }

  @Test
  public void testMarshalUnmarshalVerlegd() {
    BtwPercentage expected = new BtwPercentage(0.2, true);
    assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
  }

  @Test
  public void testMarhalUnmarshalNotVerlegd() {
    BtwPercentage expected = new BtwPercentage(0.3, false);
    assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
  }
}
