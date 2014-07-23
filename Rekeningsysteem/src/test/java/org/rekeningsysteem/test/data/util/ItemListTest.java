package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.logic.bedragmanager.Totalen;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

@RunWith(MockitoJUnitRunner.class)
public class ItemListTest extends EqualsHashCodeTest {

	private ItemList<ListItem> list;
	@Mock private ListItem item1;
	
	@Override
	protected ItemList<ListItem> makeInstance() {
		return new ItemList<>();
	}

	@Override
	protected ItemList<ListItem> makeNotInstance() {
		when(this.item1.getLoon()).thenReturn(new Geld(1.00));
		when(this.item1.getMateriaal()).thenReturn(new Geld(1.00));
		when(this.item1.getTotaal()).thenReturn(new Geld(2.00));
		
		ItemList<ListItem> l = new ItemList<>();
		l.add(this.item1);
		return l;
	}

	@Before
	public void setUp() {
		super.setUp();
		this.list = this.makeInstance();

		when(this.item1.getLoon()).thenReturn(new Geld(1.00));
		when(this.item1.getMateriaal()).thenReturn(new Geld(1.00));
		when(this.item1.getTotaal()).thenReturn(new Geld(2.00));
	}
	
	@Test
	public void testGetTotalenEmptyList() {
		assertEquals(new Totalen(), this.list.getTotalen());
	}

	@Test
	public void testGetTotalenNonEmptyList() {
		this.list.add(this.item1);
		
		assertEquals(new Totalen().withLoon(new Geld(1.00))
				.withMateriaal(new Geld(1.00)), this.list.getTotalen());
	}

	@Test
	public void testSize() {
		this.list.add(this.item1);
		this.list.add(this.item1);
		this.list.add(this.item1);
		
		assertEquals(3, this.list.size());
	}

	@Test
	public void testIsEmptyTrue() {
		assertTrue(this.list.isEmpty());
	}

	@Test
	public void testIsEmptyFalse() {
		this.testSize();
		
		assertFalse(this.list.isEmpty());
	}

	@Test
	public void testAdd() {
		assertTrue(this.list.add(this.item1));
		
		assertEquals(new Totalen().withLoon(new Geld(1.00))
				.withMateriaal(new Geld(1.00)), this.list.getTotalen());
	}

	@Test
	public void testAddMore() {
		this.list = new ItemList<>(Arrays.asList(this.item1, this.item1, this.item1));
		
		assertEquals(new Totalen().withLoon(new Geld(3.00))
				.withMateriaal(new Geld(3.00)), this.list.getTotalen());
	}

	@Test
	public void testRemove() {
		this.testAdd();
		
		assertTrue(this.list.remove(this.item1));
		
		assertTrue(this.list.isEmpty());
		assertEquals(new Totalen(), this.list.getTotalen());
	}

	@Test
	public void testClear() {
		this.testAddMore();
		this.list.clear();
		
		assertTrue(this.list.isEmpty());
		assertEquals(new Totalen(), this.list.getTotalen());
	}

	@Test
	public void testEqualsFalseOtherBtwPercentage() {
		ListItem mock = mock(ListItem.class);
		when(mock.getLoon()).thenReturn(new Geld(1.00));
		when(mock.getMateriaal()).thenReturn(new Geld(2.00));
		
		ItemList<ListItem> list2 = new ItemList<>();
		list2.add(mock);
		
		assertFalse(this.list.equals(list2));
	}

	@Test
	public void testToString() {
		assertEquals("<ItemList[[], <Totalen[<Geld[0,00]>, <Geld[0,00]>, <Geld[0,00]>, "
				+ "<Geld[0,00]>, <Geld[0,00]>, <Geld[0,00]>]>]>", this.list.toString());
	}
}
