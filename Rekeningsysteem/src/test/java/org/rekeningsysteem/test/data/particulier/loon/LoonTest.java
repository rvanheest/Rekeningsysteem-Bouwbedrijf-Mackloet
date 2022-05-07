package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.loon.Loon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.util.BtwListItemTest;

public abstract class LoonTest extends BtwListItemTest {

	private Loon loon;

	@Override
	protected abstract Loon makeInstance();

	protected abstract Loon makeInstance(boolean verlegd);

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.loon = this.makeInstance();
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(new Geld(0), this.loon.materiaal());
	}

	@Test
	public void testGetMateriaalBtwPercentageNietVerlegd() {
		assertEquals(new BtwPercentage(0.0, false), this.makeInstance(false).materiaalBtwPercentage());
	}

	@Test
	public void testGetMateriaalBtwPercentageWelVerlegd() {
		assertEquals(new BtwPercentage(0.0, true), this.makeInstance(true).materiaalBtwPercentage());
	}

	@Test
	@Override
	public void testGetMateriaalBtw() {
		assertEquals(new Geld(0), this.loon.materiaalBtw());
	}
}
