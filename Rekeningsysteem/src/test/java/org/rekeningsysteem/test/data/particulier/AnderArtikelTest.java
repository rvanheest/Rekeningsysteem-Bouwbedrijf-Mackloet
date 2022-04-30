package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public class AnderArtikelTest extends ParticulierArtikelTest {

	private AnderArtikel artikel;
	private final Geld prijs = new Geld(21);
	private final BtwPercentage btwPercentage = new BtwPercentage(10, false);

	@Override
	protected AnderArtikel makeInstance() {
		return new AnderArtikel(this.getTestOmschrijving(), this.prijs, this.btwPercentage);
	}

	@Override
	protected AnderArtikel makeNotInstance() {
		return new AnderArtikel(this.getTestOmschrijving() + ". ", this.prijs, this.btwPercentage);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.artikel = this.makeInstance();
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(this.prijs, this.artikel.materiaal());
	}

	@Test
	public void testGetMateriaalBtwPercentage() {
		assertEquals(this.btwPercentage, this.artikel.getMateriaalBtwPercentage());
	}

	@Test
	public void testEqualsFalseOtherPrijs() {
		AnderArtikel aa2 = new AnderArtikel(this.getTestOmschrijving(), this.prijs.multiply(2), this.btwPercentage);
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testEqualsFalseOtherBtw() {
		AnderArtikel aa2 = new AnderArtikel(this.getTestOmschrijving(), this.prijs, new BtwPercentage(this.btwPercentage.percentage() + 1, false));
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testToString() {
		assertEquals("<AnderArtikel[omschrijving, Geld[bedrag=21.0], BtwPercentage[percentage=10.0, verlegd=false]]>", this.artikel.toString());
	}
}
