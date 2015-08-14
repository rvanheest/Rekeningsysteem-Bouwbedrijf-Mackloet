package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.util.BtwListItemTest;

public abstract class ParticulierArtikelTest extends BtwListItemTest {

	private ParticulierArtikel item;

	@Override
	protected abstract ParticulierArtikel makeInstance();

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetLoon() {
		assertEquals(new Geld(0), this.item.getLoon());
	}

	@Test
	public void testGetLoonBtwPercentage() {
		assertEquals(0.0, this.item.getLoonBtwPercentage(), 0.0);
	}
}
