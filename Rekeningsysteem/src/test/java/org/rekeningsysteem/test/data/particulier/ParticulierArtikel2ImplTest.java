package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.util.Geld;

// TODO AnderArtikelTest
public class ParticulierArtikel2ImplTest extends ParticulierArtikel2Test {

	private ParticulierArtikel2Impl artikel;
	private final Geld prijs = new Geld(21);
	private final double btwPercentage = 10;

	@Override
	protected ParticulierArtikel2Impl makeInstance() {
		return new ParticulierArtikel2Impl(this.getTestOmschrijving(), this.prijs, this.btwPercentage);
	}

	@Override
	protected ParticulierArtikel2Impl makeNotInstance() {
		return new ParticulierArtikel2Impl(this.getTestOmschrijving() + ". ", this.prijs, this.btwPercentage);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.artikel = this.makeInstance();
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(this.prijs, this.artikel.getMateriaal());
	}

	@Test
	public void testGetMateriaalBtwPercentage() {
		assertEquals(this.btwPercentage, this.artikel.getMateriaalBtwPercentage(), 0.0);
	}

	@Test
	public void testEqualsFalseOtherPrijs() {
		ParticulierArtikel2Impl aa2 = new ParticulierArtikel2Impl(this.getTestOmschrijving(), this.prijs.multiply(2),
				this.btwPercentage);
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testEqualsFalseOtherBtw() {
		ParticulierArtikel2Impl aa2 = new ParticulierArtikel2Impl(this.getTestOmschrijving(), this.prijs,
				this.btwPercentage + 1);
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testToString() {
		assertEquals("<ParticulierArtikelImpl[omschrijving, <Geld[21,00]>, 10.0]>",
				this.artikel.toString());
	}
}
