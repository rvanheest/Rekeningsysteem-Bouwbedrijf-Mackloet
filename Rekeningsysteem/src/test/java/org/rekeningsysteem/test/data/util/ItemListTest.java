package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.BtwListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

@RunWith(MockitoJUnitRunner.class)
public class ItemListTest extends EqualsHashCodeTest {

	private ItemList<ListItem> list;
	@Mock private ListItem item;
	@Mock private BtwListItem btwItem;

	@Override
	protected ItemList<ListItem> makeInstance() {
		when(this.item.getLoon()).thenReturn(new Geld(1));
		when(this.item.getMateriaal()).thenReturn(new Geld(2));

		when(this.btwItem.getLoon()).thenReturn(new Geld(4));
		when(this.btwItem.getLoonBtwPercentage()).thenReturn(50.0);
		when(this.btwItem.getLoonBtw()).thenReturn(new Geld(2));
		when(this.btwItem.getMateriaal()).thenReturn(new Geld(5));
		when(this.btwItem.getMateriaalBtwPercentage()).thenReturn(100.0);
		when(this.btwItem.getMateriaalBtw()).thenReturn(new Geld(5));

		ItemList<ListItem> l = new ItemList<>();
		l.add(this.item);
		l.add(this.btwItem);
		return l;
	}

	@Override
	protected ItemList<ListItem> makeNotInstance() {
		when(this.item.getLoon()).thenReturn(new Geld(1.00));
		when(this.item.getMateriaal()).thenReturn(new Geld(2.00));

		ItemList<ListItem> l = new ItemList<>();
		l.add(this.item);
		return l;
	}

	@Before
	public void setUp() {
		super.setUp();
		this.list = this.makeInstance();
	}

	@Test
	public void testGetTotalenEmptyList() {
		assertEquals(new Totalen(), new ItemList<>().getTotalen());
	}

	@Test
	public void testGetTotalenNonEmptyList() {
		this.list.add(this.item);
		this.list.add(this.btwItem);
		this.list.add(this.item);
		this.list.add(this.btwItem);
		this.list.add(this.item);
		this.list.add(this.btwItem);

		Totalen result = this.list.getTotalen();

		assertEquals(new Geld(20), result.getLoon());
		assertEquals(new Geld(28), result.getMateriaal());
		assertEquals(new Geld(8), result.getBtw().get(50.0));
		assertEquals(new Geld(20), result.getBtw().get(100.0));
		assertEquals(new Geld(48), result.getSubtotaal());
		assertEquals(new Geld(72), result.getTotaal());
	}
}
