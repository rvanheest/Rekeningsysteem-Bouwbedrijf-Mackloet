package org.rekeningsysteem.test.io.xml.adaptee.reparaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;

public class ReparatiesBonAdapteeTest {

	private ReparatiesBonAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ReparatiesBonAdaptee();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.adaptee.setOmschrijving("omschr");
		assertEquals("omschr", this.adaptee.getOmschrijving());
	}

	@Test
	public void testSetGetBonnummer() {
		this.adaptee.setBonnummer("bonnr");
		assertEquals("bonnr", this.adaptee.getBonnummer());
	}

	@Test
	public void testSetGetLoon() {
		Geld loon = new Geld(12.04);
		this.adaptee.setLoon(loon);
		assertEquals(loon, this.adaptee.getLoon());
	}

	@Test
	public void testSetGetMateriaal() {
		Geld materiaal = new Geld(16.40);
		this.adaptee.setMateriaal(materiaal);
		assertEquals(materiaal, this.adaptee.getMateriaal());
	}
}
