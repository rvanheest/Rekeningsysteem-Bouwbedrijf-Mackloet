package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class TotalenTest extends EqualsHashCodeTest {

	private Totalen totalen;

	@Override
	protected Totalen makeInstance() {
		return new Totalen(new BtwPercentage(50.0, false), new Geld(2), new Geld(16));
	}

	@Override
	protected Totalen makeNotInstance() {
		return new Totalen(new BtwPercentage(0.0, false), new Geld(1), new Geld(0));
	}

	@Before
	public void setUp() {
		super.setUp();
		this.totalen = this.makeInstance();
	}

	@Test
	public void testAdd() {
		this.totalen = this.totalen.add(new BtwPercentage(50, false), new Geld(0), new Geld(20))
				.add(new BtwPercentage(50, false), new Geld(0), new Geld(30))
				.add(new BtwPercentage(10, true), new Geld(0), new Geld(100))
				.add(new BtwPercentage(20, true), new Geld(0), new Geld(100));

		assertEquals(new Geld(2), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).netto());
		assertEquals(new Geld(66), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).btw());
		assertEquals(new Geld(0), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(10.0, true)).netto());
		assertEquals(new Geld(100), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(10.0, true)).btw());
		assertEquals(new Geld(0), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(20.0, true)).netto());
		assertEquals(new Geld(100), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(20.0, true)).btw());
		assertEquals(new Geld(2), this.totalen.getSubtotaal());
		assertEquals(new Geld(68), this.totalen.getTotaal()); // 2 + 16 + 20 + 30
	}

	@Test
	public void testGetSubtotalen() {
		assertEquals(new Geld(2), this.totalen.getSubtotaal());
	}

	@Test
	public void testGetTotalen() {
		assertEquals(new Geld(18), this.totalen.getTotaal());
	}

	@Test
	public void testPlus() {
		Totalen t2 = new Totalen(new BtwPercentage(20, true), new Geld(3), new Geld(100));

		Totalen expected = Totalen.Empty()
				.add(new BtwPercentage(50, false), new Geld(2), new Geld(16))
				.add(new BtwPercentage(20, true), new Geld(3), new Geld(100));

		assertEquals(expected, this.totalen.plus(t2));
	}
}
