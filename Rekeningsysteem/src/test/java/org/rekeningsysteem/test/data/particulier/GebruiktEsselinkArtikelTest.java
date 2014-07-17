package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;

public class GebruiktEsselinkArtikelTest extends ParticulierArtikelTest {

	private GebruiktEsselinkArtikel gebruiktArtikel;
	private final EsselinkArtikel artikel = new EsselinkArtikel("artikelnummer", "omschrijving", 2,
			"eenheid", new Geld(1));
	private final double aantal = 5;

	@Override
	protected Object makeInstance() {
		return new GebruiktEsselinkArtikel(this.artikel, this.aantal);
	}

	@Override
	protected Object makeNotInstance() {
		return new GebruiktEsselinkArtikel(this.artikel, this.aantal + 1);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.gebruiktArtikel = new GebruiktEsselinkArtikel(this.artikel, this.aantal);
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
	@Override
	public void testGetLoon() {
		assertEquals(new Geld(0), this.gebruiktArtikel.getLoon());
	}

	@Test
	@Override
	public void testGetMateriaal() {
		assertEquals(new Geld(2.5), this.gebruiktArtikel.getMateriaal());
	}

	@Test
	@Override
	public void testGetTotaal() {
		assertEquals(new Geld(2.5), this.gebruiktArtikel.getTotaal());
	}
	
	@Test
	@Override
	public void testToArray() {
		assertArrayEquals(new String[] {
				"artikelnummer",
				"omschrijving",
				"2",
				"eenheid",
				new Geld(1).formattedString(),
				"5.0"
		}, this.gebruiktArtikel.toArray());
	}

	@Test
	public void testEqualsFalseOtherEsselinkAritkel() {
		EsselinkArtikel ea = new EsselinkArtikel("", "", 1, "", new Geld(2));
		GebruiktEsselinkArtikel gea = new GebruiktEsselinkArtikel(ea, this.aantal);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testEqualsFalseOtherAantal() {
		double aant = 4;
		GebruiktEsselinkArtikel gea = new GebruiktEsselinkArtikel(this.artikel, aant);
		assertFalse(this.gebruiktArtikel.equals(gea));
	}

	@Test
	public void testToString() {
		assertEquals(
				"<GebruiktEsselinkArtikel[<EsselinkArtikel[artikelnummer, omschrijving, 2, "
						+ "eenheid, <Geld[1,00]>]>, 5.0]>",
				this.gebruiktArtikel.toString());
	}
}
