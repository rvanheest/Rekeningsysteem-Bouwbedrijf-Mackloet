package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public abstract class ListItemTest extends EqualsHashCodeTest {

	private ListItem item;

	@Override
	protected abstract ListItem makeInstance();

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetTotaal() {
		Geld loon = this.item.getLoon();
		Geld materiaal = this.item.getMateriaal();

		Geld expected = new Geld(loon.getBedrag() + materiaal.getBedrag());
		assertEquals(expected, this.item.getTotaal());
	}
}
