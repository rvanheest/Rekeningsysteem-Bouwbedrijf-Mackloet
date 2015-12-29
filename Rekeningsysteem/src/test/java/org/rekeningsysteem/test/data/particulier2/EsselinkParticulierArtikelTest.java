package org.rekeningsysteem.test.data.particulier2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.util.Geld;

// TODO GebruiktEsselinkArtikelTest
public class EsselinkParticulierArtikelTest extends ParticulierArtikel2Test {

	private EsselinkParticulierArtikel gebruiktArtikel;
	private final EsselinkArtikel artikel = new EsselinkArtikel("artikelnummer", "omschrijving", 2,
			"eenheid", new Geld(1));
	private final double aantal = 5;
	private final double btwPercentage = 10;

	@Override
	protected EsselinkParticulierArtikel makeInstance() {
		return new EsselinkParticulierArtikel(this.getTestOmschrijving(), this.artikel, this.aantal, this.btwPercentage);
	}

	@Override
	protected EsselinkParticulierArtikel makeNotInstance() {
		return new EsselinkParticulierArtikel(this.getTestOmschrijving(), this.artikel, this.aantal + 1, this.btwPercentage);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.gebruiktArtikel = this.makeInstance();
	}

	@Test
	public void testGetArtikel() {
		assertEquals(this.artikel, this.gebruiktArtikel.getArtikel());
	}

	@Test
	public void testGetAantal() {
		assertEquals(this.aantal, this.gebruiktArtikel.getAantal(), 0.0);
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(new Geld(2.5), this.gebruiktArtikel.getMateriaal());
	}

	@Test
	public void testGetMateriaalBtwPercentage() {
		assertEquals(this.btwPercentage, this.gebruiktArtikel.getMateriaalBtwPercentage(), 0.0);
	}

	@Test
	public void testEqualsFalseOtherEsselinkAritkel() {
		EsselinkArtikel ea = new EsselinkArtikel("", "", 1, "", new Geld(2));
		EsselinkParticulierArtikel gea = new EsselinkParticulierArtikel(ea, this.aantal,
				this.btwPercentage);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testEqualsFalseOtherAantal() {
		EsselinkParticulierArtikel gea = new EsselinkParticulierArtikel(this.artikel, this.aantal + 1,
				this.btwPercentage);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testEqualsFalseOtherMateriaalBtwPercentage() {
		EsselinkParticulierArtikel gea = new EsselinkParticulierArtikel(this.artikel, this.aantal,
				this.btwPercentage + 1);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testToString() {
		assertEquals("<EsselinkParticulierArtikel[omschrijving, <Geld[2,50]>, 10.0, 5.0, "
				+ "<EsselinkArtikel[artikelnummer, omschrijving, 2, eenheid, <Geld[1,00]>]>]>",
				this.gebruiktArtikel.toString());
	}
}
