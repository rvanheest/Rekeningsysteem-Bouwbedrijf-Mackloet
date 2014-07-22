package org.rekeningsysteem.test.data.util.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public abstract class AbstractLoonTest extends EqualsHashCodeTest {

	private AbstractLoon loon;
	private String omschrijving = "omschrijving";

	@Override
	protected abstract AbstractLoon makeInstance();

	protected AbstractLoon getInstance() {
		return this.loon;
	}

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
	public void testEqualsFalseOtherOmschrijving() {
		this.omschrijving = "foo";
		assertFalse(this.makeInstance().equals(this.loon));
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(new Geld(0), this.loon.getMateriaal());
	}

	@Test
	public abstract void testGetTotaal();

	@Test
	public void testGetTotaalEqualsToLoon() {
		assertEquals(this.loon.getLoon(), this.loon.getTotaal());
	}
}
