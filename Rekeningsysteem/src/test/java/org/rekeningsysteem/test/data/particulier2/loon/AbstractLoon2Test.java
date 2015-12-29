package org.rekeningsysteem.test.data.particulier2.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier2.loon.AbstractLoon2;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.particulier2.ParticulierArtikel2Test;

public abstract class AbstractLoon2Test extends ParticulierArtikel2Test {

	private AbstractLoon2 loon;

	@Override
	protected abstract AbstractLoon2 makeInstance();

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
