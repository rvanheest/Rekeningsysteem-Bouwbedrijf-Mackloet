package org.rekeningsysteem.test.io.xml.adaptee.util.header;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adaptee.util.header.DebiteurAdaptee;

public class DebiteurAdapteeTest {

	private DebiteurAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new DebiteurAdaptee();
	}

	@Test
	public void testSetGetNaam() {
		this.adaptee.setNaam("Naam");
		assertEquals("Naam", this.adaptee.getNaam());
	}

	@Test
	public void testSetGetStraat() {
		this.adaptee.setStraat("Straat");
		assertEquals("Straat", this.adaptee.getStraat());
	}

	@Test
	public void testSetGetNummer() {
		this.adaptee.setNummer("Nummer");
		assertEquals("Nummer", this.adaptee.getNummer());
	}

	@Test
	public void testSetGetPostcode() {
		this.adaptee.setPostcode("Postcode");
		assertEquals("Postcode", this.adaptee.getPostcode());
	}

	@Test
	public void testSetGetPlaats() {
		this.adaptee.setPlaats("Plaats");
		assertEquals("Plaats", this.adaptee.getPlaats());
	}

	@Test
	public void testSetGetBtwNummer() {
		this.adaptee.setBtwNummer(Optional.of("BtwNummer"));
		assertEquals(Optional.of("BtwNummer"), this.adaptee.getBtwNummer());
	}
}
