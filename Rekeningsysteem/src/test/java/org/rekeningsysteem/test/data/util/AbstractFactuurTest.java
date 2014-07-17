package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Test;
import org.mockito.Mock;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ListItem;

public abstract class AbstractFactuurTest<E extends ListItem> extends AbstractRekeningTest {

	private final String valuta = "euro";
	private final BtwPercentage btwPercentage = new BtwPercentage(6, 21);
	@Mock private E item;
	@Mock private E item2;

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
		assertEquals(new ArrayList<E>(), this.getInstance().getItemList());
	}

	@Test
	public void testGetBtwPercentage() {
		assertEquals(this.getTestBtwPercentage(), this.getInstance().getBtwPercentage());
	}

	@Test
	public void testSetBtwPercentage() {
		this.getInstance().setBtwPercentage(new BtwPercentage(13, 12));
		assertEquals(new BtwPercentage(13, 12), this.getInstance().getBtwPercentage());
	}

	@Test
	public void testEqualsFalseOtherBtwPercentage() {
		AbstractFactuur<E> factuur2 = this.makeInstance();
		factuur2.setBtwPercentage(new BtwPercentage(
				this.getTestBtwPercentage().getLoonPercentage() + 1,
				this.getTestBtwPercentage().getMateriaalPercentage()));
		assertFalse(this.getInstance().equals(factuur2));
	}

	@Test
	public void testEqualsFalseOtherItemList() {
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
