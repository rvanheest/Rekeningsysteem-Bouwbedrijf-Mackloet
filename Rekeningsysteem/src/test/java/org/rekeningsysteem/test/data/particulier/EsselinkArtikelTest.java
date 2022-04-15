package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class EsselinkArtikelTest extends EqualsHashCodeTest {

	private EsselinkArtikel artikel;
	private final String artikelNummer = "artikelNummer";
	private final String omschrijving = "omschrijving";
	private final int prijsPer = 1;
	private final String eenheid = "eenheid";
	private final Geld prijs = new Geld(3);

	@Override
	protected Object makeInstance() {
		return new EsselinkArtikel(this.artikelNummer, this.omschrijving, this.prijsPer,
				this.eenheid, this.prijs);
	}

	@Override
	protected Object makeNotInstance() {
		return new EsselinkArtikel(this.artikelNummer + ".", this.omschrijving, this.prijsPer,
				this.eenheid, this.prijs);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.artikel = new EsselinkArtikel(this.artikelNummer, this.omschrijving, this.prijsPer,
				this.eenheid, this.prijs);
	}

	@Test
	public void testGetArtikelNummer() {
		assertEquals(this.artikelNummer, this.artikel.artikelNummer());
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.artikel.omschrijving());
	}

	@Test
	public void testGetPrijsPer() {
		assertEquals(this.prijsPer, this.artikel.prijsPer());
	}

	@Test
	public void testGetEenheid() {
		assertEquals(this.eenheid, this.artikel.eenheid());
	}

	@Test
	public void testGetVerkoopPrijs() {
		assertEquals(this.prijs, this.artikel.verkoopPrijs());
	}

	@Test
	public void testEqualsFalseOtherArtikelNummer() {
		EsselinkArtikel ea2 = new EsselinkArtikel(this.artikelNummer + ".", this.omschrijving, this.prijsPer, this.eenheid, this.prijs);
		assertFalse(this.artikel.equals(ea2));
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		EsselinkArtikel ea2 = new EsselinkArtikel(this.artikelNummer, this.omschrijving + ".", this.prijsPer, this.eenheid, this.prijs);
		assertFalse(this.artikel.equals(ea2));
	}

	@Test
	public void testEqualsFalseOtherPrijsPer() {
		EsselinkArtikel ea2 = new EsselinkArtikel(this.artikelNummer, this.omschrijving, this.prijsPer + 1, this.eenheid, this.prijs);
		assertFalse(this.artikel.equals(ea2));
	}

	@Test
	public void testEqualsFalseOtherEenheid() {
		EsselinkArtikel ea2 = new EsselinkArtikel(this.artikelNummer, this.omschrijving, this.prijsPer, this.eenheid + ".", this.prijs);
		assertFalse(this.artikel.equals(ea2));
	}

	@Test
	public void testEqualsFalseOtherPrijs() {
		EsselinkArtikel ea2 = new EsselinkArtikel(this.artikelNummer, this.omschrijving, this.prijsPer, this.eenheid, new Geld(1));
		assertFalse(this.artikel.equals(ea2));
	}
}
