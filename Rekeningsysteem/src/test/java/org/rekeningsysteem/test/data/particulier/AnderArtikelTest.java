package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.util.Geld;

public class AnderArtikelTest extends ParticulierArtikelTest {

	private AnderArtikel artikel;
	private final String omschrijving = "omschrijving";
	private final Geld prijs = new Geld(21);

	@Override
	protected Object makeInstance() {
		return new AnderArtikel(this.omschrijving, this.prijs);
	}

	@Override
	protected Object makeNotInstance() {
		return new AnderArtikel(this.omschrijving + ". ", this.prijs);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.artikel = new AnderArtikel(this.omschrijving, this.prijs);
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.artikel.getOmschrijving());
	}

	@Test
	@Override
	public void testGetLoon() {
		assertEquals(new Geld(0), this.artikel.getLoon());
	}

	@Test
	@Override
	public void testGetMateriaal() {
		assertEquals(this.prijs, this.artikel.getMateriaal());
	}

	@Test
	@Override
	public void testGetTotaal() {
		assertEquals(this.prijs, this.artikel.getTotaal());
	}
	
	@Test
	@Override
	public void testToArray() {
		assertArrayEquals(new String[] {
				"",
				this.omschrijving,
				"",
				"",
				this.prijs.formattedString(),
				""
		}, this.artikel.toArray());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		AnderArtikel aa2 = new AnderArtikel(this.omschrijving + ".", this.prijs);
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testEqualsFalseOtherPrijs() {
		AnderArtikel aa2 = new AnderArtikel(this.omschrijving, new Geld(30));
		assertFalse(this.artikel.equals(aa2));
	}

	@Test
	public void testToString() {
		assertEquals("<AnderArtikel[omschrijving, <Geld[21,00]>]>", this.artikel.toString());
	}
}
