package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.loon.AbstractLoon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.particulier.ParticulierArtikelTest;

public abstract class AbstractLoonTest extends ParticulierArtikelTest {

	private AbstractLoon loon;

	@Override
	protected abstract AbstractLoon makeInstance();

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.loon = this.makeInstance();
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
}
