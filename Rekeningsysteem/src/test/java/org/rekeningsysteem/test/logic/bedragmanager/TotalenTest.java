package org.rekeningsysteem.test.logic.bedragmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.logic.bedragmanager.Totalen;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class TotalenTest extends EqualsHashCodeTest {

	private Totalen totalen;

	@Override
	protected Totalen makeInstance() {
		return new Totalen().withLoon(new Geld(1.00))
				.withLoonBtw(new Geld(1.00))
				.withMateriaal(new Geld(1.00))
				.withMateriaalBtw(new Geld(1.00));
	}

	@Override
	protected Totalen makeNotInstance() {
		return new Totalen().withLoon(new Geld(1.00));
	}
	
	@Before
	public void setUp() {
		super.setUp();
		this.totalen = this.makeInstance();
	}

	@Test
	public void testGetLoon() {
		assertEquals(new Geld(1.00), this.totalen.getLoon());
	}

	@Test
	public void testGetLoonBtw() {
		assertEquals(new Geld(1.00), this.totalen.getLoonBtw());
	}
	
	@Test
	public void testGetMateriaal() {
		assertEquals(new Geld(1.00), this.totalen.getMateriaal());
	}

	@Test
	public void testGetMateriaalBtw() {
		assertEquals(new Geld(1.00), this.totalen.getMateriaalBtw());
	}

	@Test
	public void testGetSubtotalen() {
		assertEquals(new Geld(2.00), this.totalen.getSubtotaal());
	}

	@Test
	public void testGetTotalen() {
		assertEquals(new Geld(4.00), this.totalen.getTotaal());
	}
	
	@Test
	public void testPlus() {
		Totalen t2 = new Totalen().withLoon(new Geld(2.00))
				.withLoonBtw(new Geld(1.00))
				.withMateriaal(new Geld(1.00))
				.withMateriaalBtw(new Geld(1.00));
		Totalen expected = new Totalen().withLoon(new Geld(3.00))
				.withLoonBtw(new Geld(2.00))
				.withMateriaal(new Geld(2.00))
				.withMateriaalBtw(new Geld(2.00));
		
		assertEquals(expected, this.totalen.plus(t2));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		Totalen t2 = new Totalen().withLoon(new Geld(2.00))
				.withLoonBtw(new Geld(1.00))
				.withMateriaal(new Geld(1.00))
				.withMateriaalBtw(new Geld(1.00));
		assertFalse(this.totalen.equals(t2));
	}
	
	@Test
	public void testEqualsFalseOtherLoonBtw() {
		Totalen t2 = new Totalen().withLoon(new Geld(1.00))
				.withLoonBtw(new Geld(2.00))
				.withMateriaal(new Geld(1.00))
				.withMateriaalBtw(new Geld(1.00));
		assertFalse(this.totalen.equals(t2));
	}
	
	@Test
	public void testEqualsFalseOtherMateriaal() {
		Totalen t2 = new Totalen().withLoon(new Geld(1.00))
				.withLoonBtw(new Geld(1.00))
				.withMateriaal(new Geld(2.00))
				.withMateriaalBtw(new Geld(1.00));
		assertFalse(this.totalen.equals(t2));
	}
	
	@Test
	public void testEqualsFalseOtherMateriaalBtw() {
		Totalen t2 = new Totalen().withLoon(new Geld(1.00))
				.withLoonBtw(new Geld(1.00))
				.withMateriaal(new Geld(1.00))
				.withMateriaalBtw(new Geld(2.00));
		assertFalse(this.totalen.equals(t2));
	}

	@Test
	public void testToString() {
		assertEquals("<Totalen[<Geld[1,00]>, <Geld[1,00]>, <Geld[1,00]>, <Geld[1,00]>, "
				+ "<Geld[2,00]>, <Geld[4,00]>]>", this.totalen.toString());
	}
}
