package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier.EsselinkArtikelAdaptee;

public class EsselinkArtikelAdapteeTest {

	private final String artikelNummer = "ArtikelNummer";
	private final String omschrijving = "Omschrijving";
	private final int prijsPer = 14;
	private final String eenheid = "Eenheid";
	private final Geld verkoopPrijs = new Geld(43.16);
	private EsselinkArtikelAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = EsselinkArtikelAdaptee.build(a -> a
				.setArtikelNummer(this.artikelNummer)
				.setOmschrijving(this.omschrijving)
				.setPrijsPer(this.prijsPer)
				.setEenheid(this.eenheid)
				.setVerkoopPrijs(this.verkoopPrijs));
	}

	@Test
	public void testSetGetArtikelNummer() {
		assertEquals(this.artikelNummer, this.adaptee.getArtikelNummer());
	}

	@Test
	public void testSetGetOmschrijving() {
		assertEquals(this.omschrijving, this.adaptee.getOmschrijving());
	}

	@Test
	public void testSetGetPrijsPer() {
		assertEquals(this.prijsPer, this.adaptee.getPrijsPer());
	}

	@Test
	public void testSetGetEenheid() {
		assertEquals(this.eenheid, this.adaptee.getEenheid());
	}

	@Test
	public void testSetGetVerkoopPrijs() {
		assertEquals(this.verkoopPrijs, this.adaptee.getVerkoopPrijs());
	}
}
