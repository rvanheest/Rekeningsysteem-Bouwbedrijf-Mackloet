package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentages;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class BtwPercentagesTest extends EqualsHashCodeTest {

	private BtwPercentages btwPercentages;
	private final double loonPercentage = 6.0;
	private final double materiaalPercentage = 21.0;

	@Override
	protected BtwPercentages makeInstance() {
		return new BtwPercentages(this.loonPercentage, this.materiaalPercentage);
	}

	@Override
	protected Object makeNotInstance() {
		return new BtwPercentages(this.loonPercentage + 1, this.materiaalPercentage);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.btwPercentages = this.makeInstance();
	}

	@Test
	public void testGetMateriaalPercentage() {
		assertEquals(this.materiaalPercentage, this.btwPercentages.getMateriaalPercentage(), 0.0);
	}

	@Test
	public void testGetLoonPeercentage() {
		assertEquals(this.loonPercentage, this.btwPercentages.getLoonPercentage(), 0.0);
	}

	@Test
	public void testEqualsFalseOtherMateriaalPercentage() {
		BtwPercentages btw = new BtwPercentages(this.materiaalPercentage + 1, this.loonPercentage);
		assertFalse(this.btwPercentages.equals(btw));
	}

	@Test
	public void testEqualsFalseOTherLoonPercentage() {
		BtwPercentages btw = new BtwPercentages(this.materiaalPercentage, this.loonPercentage + 1);
		assertFalse(this.btwPercentages.equals(btw));
	}

	@Test
	public void testToString() {
		assertEquals("<BtwPercentage[6.0, 21.0]>", this.btwPercentages.toString());
	}
}
