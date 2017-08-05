package com.github.rvanheest.rekeningsysteem.test.model.normal;

import com.github.rvanheest.rekeningsysteem.model.normal.NormalListItem;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public abstract class NormalListItemTest extends TaxedListItemTest {

  private NormalListItem item;
  private final String description = "descr";

  @Override
  protected NormalListItem makeInstance() {
    return this.makeInstance(this.description);
  }

  protected abstract NormalListItem makeInstance(String description);

  @Override
  protected NormalListItem makeNotInstance() {
    return this.makeNotInstance(this.description + ".");
  }

  protected abstract NormalListItem makeNotInstance(String otherDescription);

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
  public void testEqualsFalseOtherDescription() {
    assertFalse(this.makeNotInstance().equals(this.item));
  }
}
