package org.rekeningsysteem.test.io.xml.adaptee.util.header;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adaptee.util.header.DebiteurAdaptee;

public class DebiteurAdapteeTest {

	private final String naam = "Naam";
	private final String straat = "Straat";
	private final String nummer = "Nummer";
	private final String postcode = "Postcode";
	private final String plaats = "Plaats";
	private final String btwNummer = "BtwNummer";
	private DebiteurAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = DebiteurAdaptee.build(a -> a
				.setNaam(this.naam)
				.setStraat(this.straat)
				.setNummer(this.nummer)
				.setPostcode(this.postcode)
				.setPlaats(this.plaats)
				.setBtwNummer(this.btwNummer));
	}

	@Test
	public void testSetGetNaam() {
		assertEquals(this.naam, this.adaptee.getNaam());
	}

	@Test
	public void testSetGetStraat() {
		assertEquals(this.straat, this.adaptee.getStraat());
	}

	@Test
	public void testSetGetNummer() {
		assertEquals(this.nummer, this.adaptee.getNummer());
	}

	@Test
	public void testSetGetPostcode() {
		assertEquals(this.postcode, this.adaptee.getPostcode());
	}

	@Test
	public void testSetGetPlaats() {
		assertEquals(this.plaats, this.adaptee.getPlaats());
	}

	@Test
	public void testSetGetBtwNummer() {
		assertEquals(this.btwNummer, this.adaptee.getBtwNummer());
	}
}
