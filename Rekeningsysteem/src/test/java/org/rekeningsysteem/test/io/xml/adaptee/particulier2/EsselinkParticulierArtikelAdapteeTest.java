package org.rekeningsysteem.test.io.xml.adaptee.particulier2;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier2.EsselinkParticulierArtikelAdaptee;

public class EsselinkParticulierArtikelAdapteeTest extends ParticulierArtikel2AdapteeTest {

	@Override
	protected EsselinkParticulierArtikelAdaptee makeInstance() {
		return new EsselinkParticulierArtikelAdaptee();
	}

	@Override
	protected EsselinkParticulierArtikelAdaptee getInstance() {
		return (EsselinkParticulierArtikelAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("abc");
		assertEquals("abc", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetArtikel() {
		EsselinkArtikel artikel = new EsselinkArtikel("artnr", "omschr", 12, "eenheid", new Geld(
				12.30));
		this.getInstance().setArtikel(artikel);
		assertEquals(artikel, this.getInstance().getArtikel());
	}

	@Test
	public void testSetGetAantal() {
		this.getInstance().setAantal(12.43);
		assertEquals(12.43, this.getInstance().getAantal(), 0.0);
	}

	@Test
	public void testSetGetMateriaalBtwPercentage() {
		this.getInstance().setMateriaalBtwPercentage(0.7);
		assertEquals(0.7, this.getInstance().getMateriaalBtwPercentage(), 0.0);
	}
}
