package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;

public abstract class AbstractFactuurTest<E extends ListItem> extends AbstractRekeningTest {

	private final String valuta = "euro";
	private final BtwPercentage btwPercentage = new BtwPercentage(6, 21);

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
		assertEquals(new ItemList<E>(this.getTestBtwPercentage()),
				this.getInstance().getItemList());
	}

	@Test
	public void testEqualsFalseOtherItemList() {
		AbstractFactuur<E> factuur2 = this.makeInstance();
		factuur2.getItemList().setBtwPercentage(new BtwPercentage(0.0, 0.0));
		assertFalse(this.getInstance().equals(factuur2));
	}

	@Test
	public void testEqualsFalseOtherValuta() {
		AbstractFactuur<E> factuur2 = this.makeInstance();
		factuur2.setValuta(this.getTestValuta() + "1234");
		assertFalse(this.getInstance().equals(factuur2));
	}
}
