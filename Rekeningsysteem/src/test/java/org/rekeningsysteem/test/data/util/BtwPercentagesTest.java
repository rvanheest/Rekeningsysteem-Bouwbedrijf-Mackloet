package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class BtwPercentagesTest extends EqualsHashCodeTest {

	private BtwPercentages btwPercentages;
	private final BtwPercentage loonPercentage = new BtwPercentage(6.0, false);
	private final BtwPercentage materiaalPercentage = new BtwPercentage(21.0, false);

	@Override
	protected BtwPercentages makeInstance() {
		return new BtwPercentages(this.loonPercentage, this.materiaalPercentage);
	}

	@Override
	protected Object makeNotInstance() {
		return new BtwPercentages(new BtwPercentage(this.loonPercentage.getPercentage() + 1, false), this.materiaalPercentage);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.btwPercentages = this.makeInstance();
	}

	@Test
	public void testGetMateriaalPercentage() {
		assertEquals(this.materiaalPercentage, this.btwPercentages.getMateriaalPercentage());
	}

	@Test
	public void testGetLoonPercentage() {
		assertEquals(this.loonPercentage, this.btwPercentages.getLoonPercentage());
	}

	@Test
	public void testEqualsFalseOtherMateriaalPercentage() {
		BtwPercentages btw = new BtwPercentages(new BtwPercentage(this.materiaalPercentage.getPercentage() + 1, false), this.loonPercentage);
		assertFalse(this.btwPercentages.equals(btw));
	}

	@Test
	public void testEqualsFalseOTherLoonPercentage() {
		BtwPercentages btw = new BtwPercentages(this.materiaalPercentage, new BtwPercentage(this.loonPercentage.getPercentage() + 1, false));
		assertFalse(this.btwPercentages.equals(btw));
	}

	@Test
	public void testToString() {
		assertEquals("<BtwPercentage[6.0, 21.0]>", this.btwPercentages.toString());
	}
}
