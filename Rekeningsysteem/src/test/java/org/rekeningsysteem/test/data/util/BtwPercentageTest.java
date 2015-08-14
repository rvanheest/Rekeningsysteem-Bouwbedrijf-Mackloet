package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class BtwPercentageTest extends EqualsHashCodeTest {

	private BtwPercentage btwPercentage;
	private final double loonPercentage = 6.0;
	private final double materiaalPercentage = 21.0;

	@Override
	protected BtwPercentage makeInstance() {
		return new BtwPercentage(this.loonPercentage, this.materiaalPercentage);
	}

	@Override
	protected Object makeNotInstance() {
		return new BtwPercentage(this.loonPercentage + 1, this.materiaalPercentage);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.btwPercentage = this.makeInstance();
	}

	@Test
	public void testGetMateriaalPercentage() {
		assertEquals(this.materiaalPercentage, this.btwPercentage.getMateriaalPercentage(), 0.0);
	}

	@Test
	public void testGetLoonPeercentage() {
		assertEquals(this.loonPercentage, this.btwPercentage.getLoonPercentage(), 0.0);
	}

	@Test
	public void testEqualsFalseOtherMateriaalPercentage() {
		BtwPercentage btw = new BtwPercentage(this.materiaalPercentage + 1, this.loonPercentage);
		assertFalse(this.btwPercentage.equals(btw));
	}

	@Test
	public void testEqualsFalseOTherLoonPercentage() {
		BtwPercentage btw = new BtwPercentage(this.materiaalPercentage, this.loonPercentage + 1);
		assertFalse(this.btwPercentage.equals(btw));
	}

	@Test
	public void testToString() {
		assertEquals("<BtwPercentage[6.0, 21.0]>", this.btwPercentage.toString());
	}
}