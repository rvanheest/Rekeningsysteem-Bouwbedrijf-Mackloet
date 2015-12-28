package org.rekeningsysteem.test.data.util.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.test.data.util.BtwListItemTest;

@Ignore
@Deprecated
public abstract class AbstractLoonTest extends BtwListItemTest {

	private AbstractLoon loon;
	private String omschrijving = "omschrijving";

	@Override
	protected abstract AbstractLoon makeInstance();

	protected String getTestOmschrijving() {
		return this.omschrijving;
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.loon = this.makeInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.loon.getOmschrijving());
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(new Geld(0), this.loon.getMateriaal());
	}

	@Test
	public void testGetMateriaalBtwPercentage() {
		assertEquals(0.0, this.loon.getMateriaalBtwPercentage(), 0.0);
	}

	@Test
	@Override
	public void testGetMateriaalBtw() {
		super.testGetMateriaalBtw();
		assertEquals(new Geld(0), this.loon.getMateriaalBtw());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		this.omschrijving = "foo";
		assertFalse(this.makeInstance().equals(this.loon));
	}
}
