package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;

@Ignore
@Deprecated
public class GebruiktEsselinkArtikelTest extends ParticulierArtikelTest {

	private GebruiktEsselinkArtikel gebruiktArtikel;
	private final EsselinkArtikel artikel = new EsselinkArtikel("artikelnummer", "omschrijving", 2,
			"eenheid", new Geld(1));
	private final double aantal = 5;
	private final double btwPercentage = 10;

	@Override
	protected GebruiktEsselinkArtikel makeInstance() {
		return new GebruiktEsselinkArtikel(this.artikel, this.aantal, this.btwPercentage);
	}

	@Override
	protected GebruiktEsselinkArtikel makeNotInstance() {
		return new GebruiktEsselinkArtikel(this.artikel, this.aantal + 1, this.btwPercentage);
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
		GebruiktEsselinkArtikel gea = new GebruiktEsselinkArtikel(ea, this.aantal,
				this.btwPercentage);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testEqualsFalseOtherAantal() {
		GebruiktEsselinkArtikel gea = new GebruiktEsselinkArtikel(this.artikel, this.aantal + 1,
				this.btwPercentage);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testEqualsFalseOtherMateriaalBtwPercentage() {
		GebruiktEsselinkArtikel gea = new GebruiktEsselinkArtikel(this.artikel, this.aantal,
				this.btwPercentage + 1);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testToString() {
		assertEquals("<GebruiktEsselinkArtikel[<EsselinkArtikel[artikelnummer, omschrijving, 2, "
				+ "eenheid, <Geld[1,00]>]>, 5.0, 10.0]>", this.gebruiktArtikel.toString());
	}
}
