package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.util.Geld;

public class AnderArtikelTest extends ParticulierArtikelTest {

	private AnderArtikel artikel;
	private final String omschrijving = "omschrijving";
	private final Geld prijs = new Geld(21);
	private final double btwPercentage = 10;

	@Override
	protected AnderArtikel makeInstance() {
		return new AnderArtikel(this.omschrijving, this.prijs, this.btwPercentage);
	}

	@Override
	protected AnderArtikel makeNotInstance() {
		return new AnderArtikel(this.omschrijving + ". ", this.prijs, this.btwPercentage);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.artikel = this.makeInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.artikel.getOmschrijving());
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
	public void testEqualsFalseOtherOmschrijving() {
		AnderArtikel aa2 = new AnderArtikel(this.omschrijving + ".", this.prijs,
				this.btwPercentage);
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testEqualsFalseOtherPrijs() {
		AnderArtikel aa2 = new AnderArtikel(this.omschrijving, this.prijs.multiply(2),
				this.btwPercentage);
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testToString() {
		assertEquals("<AnderArtikel[omschrijving, <Geld[21,00]>, 10.0]>", this.artikel.toString());
	}
}
