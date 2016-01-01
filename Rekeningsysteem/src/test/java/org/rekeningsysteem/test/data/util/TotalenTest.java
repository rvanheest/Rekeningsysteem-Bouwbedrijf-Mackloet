package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class TotalenTest extends EqualsHashCodeTest {

	private Totalen totalen;

	@Override
	protected Totalen makeInstance() {
		return new Totalen(50.0, new Geld(2), new Geld(16));
	}

	@Override
	protected Totalen makeNotInstance() {
		return new Totalen(0.0, new Geld(1), new Geld(0));
	}

	@Before
	public void setUp() {
		super.setUp();
		this.totalen = this.makeInstance();
	}

	@Test
	public void testAdd() {
		this.totalen = this.totalen.add(50, new Geld(0), new Geld(20))
				.add(50, new Geld(0), new Geld(30))
				.add(10, new Geld(0), new Geld(100))
				.add(20, new Geld(0), new Geld(100));

		Map<Double, Geld> expected = new HashMap<>();
		expected.put(50.0, new Geld(66));
		expected.put(10.0, new Geld(100));
		expected.put(20.0, new Geld(100));

		assertEquals(expected, this.totalen.getBtw());
		assertEquals(new Geld(2), this.totalen.getSubtotaal());
		assertEquals(new Geld(268), this.totalen.getTotaal());
	}

	@Test
	public void testGetNetto() {
		Map<Double, Geld> expected = new HashMap<>();
		expected.put(50.0, new Geld(2));
		assertEquals(expected, this.totalen.getNetto());
	}

	@Test
	public void testGetBtw() {
		Map<Double, Geld> expected = new HashMap<>();
		expected.put(50.0, new Geld(16));
		assertEquals(expected, this.totalen.getBtw());
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
		Totalen t2 = new Totalen(20, new Geld(3), new Geld(100));

		Totalen expected = new Totalen()
				.add(50, new Geld(2), new Geld(16))
				.add(20, new Geld(3), new Geld(100));

		assertEquals(expected, this.totalen.plus(t2));
	}

	@Test
	public void testEqualsFalseOtherNetto() {
		assertFalse(this.totalen.equals(new Totalen(50, new Geld(3), new Geld(16))));
	}

	@Test
	public void testEqualsFalseOtherBtwPercentage() {
		assertFalse(this.totalen.equals(new Totalen(40, new Geld(2), new Geld(16))));
	}

	@Test
	public void testEqualsFalseOtherBtw() {
		assertFalse(this.totalen.equals(new Totalen(50, new Geld(2), new Geld(15))));
	}

	@Test
	public void testToString() {
		assertEquals("<Totalen[{50.0=<NettoBtwTuple[<Geld[2,00]>, <Geld[16,00]>]>}]>",
				this.totalen.toString());
	}
}
