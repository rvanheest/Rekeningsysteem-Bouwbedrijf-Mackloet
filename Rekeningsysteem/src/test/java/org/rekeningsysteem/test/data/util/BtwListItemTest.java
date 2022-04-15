package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwListItem;
import org.rekeningsysteem.data.util.Geld;

public abstract class BtwListItemTest extends ListItemTest {

	private BtwListItem item;

	@Override
	protected abstract BtwListItem makeInstance();

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetLoonBtw() {
		Geld loon = this.item.loon();
		double percentage = this.item.getLoonBtwPercentage().percentage();

		Geld expected = new Geld(loon.bedrag() * percentage / 100);
		assertEquals(expected, this.item.getLoonBtw());
	}

	@Test
	public void testGetMateriaalBtw() {
		Geld materiaal = this.item.materiaal();
		double percentage = this.item.getMateriaalBtwPercentage().percentage();

		Geld expected = new Geld(materiaal.bedrag() * percentage / 100);
		assertEquals(expected, this.item.getMateriaalBtw());
	}
}
