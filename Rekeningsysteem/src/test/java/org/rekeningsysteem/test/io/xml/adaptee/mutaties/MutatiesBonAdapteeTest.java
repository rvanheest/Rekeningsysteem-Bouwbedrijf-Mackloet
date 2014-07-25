package org.rekeningsysteem.test.io.xml.adaptee.mutaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;

public class MutatiesBonAdapteeTest {

	private MutatiesBonAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new MutatiesBonAdaptee();
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
	public void testSetGetPrijs() {
		Geld prijs = new Geld(12.04);
		this.adaptee.setPrijs(prijs);
		assertEquals(prijs, this.adaptee.getPrijs());
	}
}
