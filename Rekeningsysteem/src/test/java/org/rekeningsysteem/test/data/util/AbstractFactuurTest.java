package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.Totalen;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractFactuurTest<E extends ListItem> extends AbstractRekeningTest {

	private final String valuta = "euro";
	private final BtwPercentage btwPercentage = new BtwPercentage(50, 100);
	@Mock private E item;

	@Override
	protected abstract AbstractFactuur<E> makeInstance();

	@Override
	@SuppressWarnings("unchecked")
	protected AbstractFactuur<E> getInstance() {
		return (AbstractFactuur<E>) super.getInstance();
	}

	protected String getTestValuta() {
		return this.valuta;
	}

	protected BtwPercentage getTestBtwPercentage() {
		return this.btwPercentage;
	}

	@Test
	public void testGetValuta() {
		assertEquals(this.getTestValuta(), this.getInstance().getValuta());
	}

	@Test
	public void testSetValuta() {
		this.getInstance().setValuta(this.getTestValuta() + "foobar");
		assertEquals(this.getTestValuta() + "foobar", this.getInstance().getValuta());
	}

	@Test
	public void testGetItemList() {
		assertEquals(new ItemList<E>(), this.getInstance().getItemList());
	}

	@Test
	public void testGetBtwPercentage() {
		assertEquals(this.btwPercentage, this.getInstance().getBtwPercentage());
	}

	@Test
	public void testGetTotalen() {
		when(this.item.getLoon()).thenReturn(new Geld(1.00));
		when(this.item.getMateriaal()).thenReturn(new Geld(2.00));
		
		ItemList<E> list = this.getInstance().getItemList();
		list.add(this.item);
		list.add(this.item);
		list.add(this.item);
		
		assertEquals(new Totalen().withLoon(new Geld(3.00))
				.withLoonBtw(new Geld(1.50))
				.withMateriaal(new Geld(6.00))
				.withMateriaalBtw(new Geld(6.00)),
				this.getInstance().getTotalen());
	}

	@Test
	public void testEqualsFalseOtherItemList() {
		when(this.item.getLoon()).thenReturn(new Geld(1.00));
		when(this.item.getMateriaal()).thenReturn(new Geld(2.00));
		
		AbstractFactuur<E> factuur2 = this.makeInstance();
		factuur2.getItemList().add(this.item);
		
		assertFalse(this.getInstance().equals(factuur2));
	}

	@Test
	public void testEqualsFalseOtherValuta() {
		AbstractFactuur<E> factuur2 = this.makeInstance();
		factuur2.setValuta(this.getTestValuta() + "1234");
		assertFalse(this.getInstance().equals(factuur2));
	}
}
