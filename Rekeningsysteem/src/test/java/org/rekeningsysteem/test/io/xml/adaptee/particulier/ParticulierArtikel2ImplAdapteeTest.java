package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierArtikel2ImplAdaptee;

public class ParticulierArtikel2ImplAdapteeTest extends ParticulierArtikel2AdapteeTest {

	@Override
	protected ParticulierArtikel2ImplAdaptee makeInstance() {
		return new ParticulierArtikel2ImplAdaptee();
	}

	@Override
	protected ParticulierArtikel2ImplAdaptee getInstance() {
		return (ParticulierArtikel2ImplAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("omschr");
		assertEquals("omschr", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetPrijs() {
		Geld prijs = new Geld(12.00);
		this.getInstance().setPrijs(prijs);
		assertEquals(prijs, this.getInstance().getPrijs());
	}

	@Test
	public void testSetGetMateriaalBtwPercentage() {
		this.getInstance().setMateriaalBtwPercentage(0.8);
		assertEquals(0.8, this.getInstance().getMateriaalBtwPercentage(), 0.0);
	}
}
