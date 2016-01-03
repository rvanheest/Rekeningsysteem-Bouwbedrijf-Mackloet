package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

@RunWith(MockitoJUnitRunner.class)
public class GebruiktEsselinkArtikelTest extends ParticulierArtikelTest {

	private GebruiktEsselinkArtikel gebruiktArtikel;
	private final EsselinkArtikel artikel = new EsselinkArtikel("artikelnummer", "omschrijving", 2,
			"eenheid", new Geld(1));
	private final double aantal = 5;
	private final double btwPercentage = 10;
	@Mock private ListItemVisitor<Object> visitor;

	@Override
	protected GebruiktEsselinkArtikel makeInstance() {
		return new GebruiktEsselinkArtikel(this.getTestOmschrijving(), this.artikel, this.aantal, this.btwPercentage);
	}

	@Override
	protected GebruiktEsselinkArtikel makeNotInstance() {
		return new GebruiktEsselinkArtikel(this.getTestOmschrijving(), this.artikel, this.aantal + 1, this.btwPercentage);
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
	public void testAccept() {
		this.gebruiktArtikel.accept(this.visitor);

		verify(this.visitor).visit(eq(this.gebruiktArtikel));
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
		assertEquals("<GebruiktEsselinkArtikel[omschrijving, <Geld[2,50]>, 10.0, 5.0, "
				+ "<EsselinkArtikel[artikelnummer, omschrijving, 2, eenheid, <Geld[1,00]>]>]>",
				this.gebruiktArtikel.toString());
	}
}
