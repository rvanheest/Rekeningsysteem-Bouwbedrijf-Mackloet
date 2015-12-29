package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;

@Ignore
@Deprecated
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

	@Test
	public void testSetGetMateriaalBtwPercentage() {
		this.getInstance().setMateriaalBtwPercentage(0.8);
		assertEquals(0.8, this.getInstance().getMateriaalBtwPercentage(), 0.0);
	}
}
