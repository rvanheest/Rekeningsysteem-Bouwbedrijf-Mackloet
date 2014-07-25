package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier.EsselinkArtikelAdaptee;

public class EsselinkArtikelAdapteeTest {

	private EsselinkArtikelAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new EsselinkArtikelAdaptee();
	}

	@Test
	public void testSetGetArtikelNummer() {
		this.adaptee.setArtikelNummer("ArtikelNummer");
		assertEquals("ArtikelNummer", this.adaptee.getArtikelNummer());
	}

	@Test
	public void testSetGetOmschrijving() {
		this.adaptee.setOmschrijving("Omschrijving");
		assertEquals("Omschrijving", this.adaptee.getOmschrijving());
	}

	@Test
	public void testSetGetPrijsPer() {
		this.adaptee.setPrijsPer(14);
		assertEquals(14, this.adaptee.getPrijsPer());
	}

	@Test
	public void testSetGetEenheid() {
		this.adaptee.setEenheid("Eenheid");
		assertEquals("Eenheid", this.adaptee.getEenheid());
	}

	@Test
	public void testSetGetVerkoopPrijs() {
		Geld verkoopPrijs = new Geld(43.16);
		this.adaptee.setVerkoopPrijs(verkoopPrijs);
		assertEquals(verkoopPrijs, this.adaptee.getVerkoopPrijs());
	}
}
