package org.rekeningsysteem.test.logic.bedragmanager;

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
		return new Totalen()
				.addLoon(new Geld(1))
				.addMateriaal(new Geld(1))
				.addBtw(50.0, new Geld(16));
	}

	@Override
	protected Totalen makeNotInstance() {
		return new Totalen().addLoon(new Geld(1));
	}

	@Before
	public void setUp() {
		super.setUp();
		this.totalen = this.makeInstance();
	}

	@Test
	public void testAddLoon() {
		this.totalen = this.totalen.addLoon(new Geld(2));

		assertEquals(new Geld(3), this.totalen.getLoon());
		assertEquals(new Geld(1), this.totalen.getMateriaal());
		assertEquals(new Geld(16), this.totalen.getBtw().get(50.0));
		assertEquals(new Geld(4), this.totalen.getSubtotaal());
		assertEquals(new Geld(12), this.totalen.getTotaal());
	}

	@Test
	public void testGetLoon() {
		assertEquals(new Geld(1), this.totalen.getLoon());
	}

	@Test
	public void testAddMateriaal() {
		this.totalen = this.totalen.addMateriaal(new Geld(2));

		assertEquals(new Geld(1), this.totalen.getLoon());
		assertEquals(new Geld(3), this.totalen.getMateriaal());
		assertEquals(new Geld(16), this.totalen.getBtw().get(50.0));
		assertEquals(new Geld(4), this.totalen.getSubtotaal());
		assertEquals(new Geld(12), this.totalen.getTotaal());
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(new Geld(1), this.totalen.getMateriaal());
	}

	@Test
	public void testAddBtw() {
		this.totalen = this.totalen.addBtw(50, new Geld(20))
				.addBtw(50, new Geld(30))
				.addBtw(10, new Geld(100))
				.addBtw(20, new Geld(100));

		Map<Double, Geld> expected = new HashMap<>();
		expected.put(50.0, new Geld(66));
		expected.put(10.0, new Geld(100));
		expected.put(20.0, new Geld(100));

		assertEquals(new Geld(1), this.totalen.getLoon());
		assertEquals(new Geld(1), this.totalen.getMateriaal());
		assertEquals(expected, this.totalen.getBtw());
		assertEquals(new Geld(2), this.totalen.getSubtotaal());
		assertEquals(new Geld(65), this.totalen.getTotaal());
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
		assertEquals(new Geld(10), this.totalen.getTotaal());
	}

	@Test
	public void testPlus() {
		Totalen t2 = new Totalen()
				.addLoon(new Geld(2))
				.addMateriaal(new Geld(1))
				.addBtw(20, new Geld(100));

		Totalen expected = new Totalen()
				.addLoon(new Geld(3))
				.addMateriaal(new Geld(2))
				.addBtw(50, new Geld(16))
				.addBtw(20, new Geld(100));

		assertEquals(expected, this.totalen.plus(t2));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		Totalen t2 = new Totalen()
				.addLoon(new Geld(2))
				.addMateriaal(new Geld(1))
				.addBtw(50, new Geld(16));
		assertFalse(this.totalen.equals(t2));
	}

	@Test
	public void testEqualsFalseOtherMateriaal() {
		Totalen t2 = new Totalen()
				.addLoon(new Geld(1))
				.addMateriaal(new Geld(2))
				.addBtw(50, new Geld(16));
		assertFalse(this.totalen.equals(t2));
	}

	@Test
	public void testEqualsFalseOtherBtw() {
		Totalen t2 = new Totalen()
				.addLoon(new Geld(1))
				.addMateriaal(new Geld(1))
				.addBtw(40, new Geld(16));
		assertFalse(this.totalen.equals(t2));
	}

	@Test
	public void testToString() {
		assertEquals("<Totalen[<Geld[1,00]>, <Geld[1,00]>, {50.0=<Geld[16,00]>}]>",
				this.totalen.toString());
	}
}
