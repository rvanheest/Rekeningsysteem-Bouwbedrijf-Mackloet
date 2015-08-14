package org.rekeningsysteem.test.io.xml.adaptee.util.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.util.loon.InstantLoonAdaptee;

public class InstantLoonAdapteeTest {

	private InstantLoonAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new InstantLoonAdaptee();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.adaptee.setOmschrijving("foobar");
		assertEquals("foobar", this.adaptee.getOmschrijving());
	}

	@Test
	public void testSetGetLoon() {
		Geld loon = new Geld(2.0);
		this.adaptee.setLoon(loon);
		assertEquals(loon, this.adaptee.getLoon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		this.adaptee.setLoonBtwPercentage(0.6);
		assertEquals(0.6, this.adaptee.getLoonBtwPercentage(), 0.0);
	}
}
