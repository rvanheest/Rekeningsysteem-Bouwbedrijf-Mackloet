package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

@RunWith(MockitoJUnitRunner.class)
public class ItemListTest extends EqualsHashCodeTest {

	private ItemList<ListItem> list;
	@Mock private ListItem item;
	
	@Override
	protected ItemList<ListItem> makeInstance() {
		return new ItemList<>();
	}

	@Override
	protected ItemList<ListItem> makeNotInstance() {
		when(this.item.getLoon()).thenReturn(new Geld(1.00));
		when(this.item.getMateriaal()).thenReturn(new Geld(1.00));
		when(this.item.getTotaal()).thenReturn(new Geld(2.00));
		
		ItemList<ListItem> l = new ItemList<>();
		l.add(this.item);
		return l;
	}

	@Before
	public void setUp() {
		super.setUp();
		this.list = this.makeInstance();

		when(this.item.getLoon()).thenReturn(new Geld(1.00));
		when(this.item.getMateriaal()).thenReturn(new Geld(2.00));
		when(this.item.getTotaal()).thenReturn(new Geld(2.00));
	}
	
	@Test
	public void testGetTotalenEmptyList() {
		assertEquals(new Totalen(), this.list.getTotalen());
	}

	@Test
	public void testGetTotalenNonEmptyList() {
		this.list.add(this.item);
		this.list.add(this.item);
		this.list.add(this.item);
		this.list.add(this.item);
		this.list.add(this.item);
		this.list.add(this.item);
		this.list.add(this.item);
		
		Totalen result = this.list.getTotalen();
		
		assertEquals(new Geld(7.00), result.getLoon());
		assertEquals(new Geld(0.00), result.getLoonBtw());
		assertEquals(new Geld(14.00), result.getMateriaal());
		assertEquals(new Geld(0.00), result.getMateriaalBtw());
	}
}
