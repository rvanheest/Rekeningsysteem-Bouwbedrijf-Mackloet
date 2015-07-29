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
		Geld loon = this.item.getLoon();
		double percentage = this.item.getLoonBtwPercentage();

		Geld expected = new Geld(loon.getBedrag() * percentage / 100);
		assertEquals(expected, this.item.getLoonBtw());
	}

	@Test
	public void testGetMateriaalBtw() {
		Geld materiaal = this.item.getMateriaal();
		double percentage = this.item.getMateriaalBtwPercentage();

		Geld expected = new Geld(materiaal.getBedrag() * percentage / 100);
		assertEquals(expected, this.item.getMateriaalBtw());
	}

	@Test
	@Override
	public void testGetTotaal() {
		double loon = this.item.getLoon().getBedrag();
		double loonBtw = this.item.getLoonBtw().getBedrag();
		double materiaal = this.item.getMateriaal().getBedrag();
		double materiaalBtw = this.item.getMateriaalBtw().getBedrag();

		Geld expected = new Geld(loon + loonBtw + materiaal + materiaalBtw);
		assertEquals(expected, this.item.getTotaal());
	}
}
