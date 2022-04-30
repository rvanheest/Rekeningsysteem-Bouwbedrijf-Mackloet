package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.FactuurHeader;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractFactuurTest<E extends ListItem> extends AbstractRekeningTest {

	private AbstractFactuur<E> factuur;
	@Mock private ItemList<E> itemList;

	@Override
	@SuppressWarnings("unchecked")
	protected AbstractFactuur<E> makeInstance() {
		return (AbstractFactuur<E>) super.makeInstance();
	}

	@Override
	protected AbstractFactuur<E> makeInstance(FactuurHeader header) {
		return this.makeInstance(header, this.itemList);
	}

	protected abstract AbstractFactuur<E> makeInstance(FactuurHeader header, ItemList<E> itemList);

	@Override
	@SuppressWarnings("unchecked")
	protected AbstractFactuur<E> makeNotInstance() {
		return (AbstractFactuur<E>) super.makeNotInstance();
	}
	
	@Override
	protected AbstractRekening makeNotInstance(FactuurHeader otherHeader) {
		return this.makeNotInstance(otherHeader, this.itemList);
	}

	protected abstract AbstractFactuur<E> makeNotInstance(FactuurHeader otherHeader, ItemList<E> itemList);

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.factuur = this.makeInstance();
	}

	@Test
	public void testGetItemList() {
		assertEquals(this.itemList, this.factuur.getItemList());
	}

	@Test
	public void testGetTotalen() {
		Totalen expected = Totalen.Empty();
		when(this.itemList.getTotalen()).thenReturn(expected);
		
		assertEquals(expected, this.factuur.getTotalen());
		verify(this.itemList).getTotalen();
	}

	@Test
	public void testEqualsFalseOtherItemList() {
		ItemList<E> otherList = new ItemList<>(Currency.getInstance("EUR"));
		AbstractFactuur<E> rekening2 = this.makeInstance(this.factuur.getFactuurHeader(), otherList);
		assertFalse(this.factuur.equals(rekening2));
	}
}
