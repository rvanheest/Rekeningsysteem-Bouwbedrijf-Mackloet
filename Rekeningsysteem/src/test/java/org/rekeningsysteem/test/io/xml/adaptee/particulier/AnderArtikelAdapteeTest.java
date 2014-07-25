package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;

public class AnderArtikelAdapteeTest extends ParticulierArtikelAdapteeTest {

	@Override
	protected AnderArtikelAdaptee makeInstance() {
		return new AnderArtikelAdaptee();
	}

	@Override
	protected AnderArtikelAdaptee getInstance() {
		return (AnderArtikelAdaptee) super.getInstance();
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
}
