package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.util.ListItemTest;

public class MutatiesBonTest extends ListItemTest {

	private MutatiesBon bon;
	private final String omschrijving = "omschrijving";
	private final String bonnummer = "bonnummer";
	private final Geld prijs = new Geld(10);

	@Override
	protected MutatiesBon makeInstance() {
		return new MutatiesBon(this.omschrijving, this.bonnummer, this.prijs);
	}

	@Override
	protected MutatiesBon makeNotInstance() {
		return new MutatiesBon(this.omschrijving + ".", this.bonnummer, this.prijs);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.bon = this.makeInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.bon.getOmschrijving());
	}

	@Test
	public void testGetBonnummer() {
		assertEquals(this.bonnummer, this.bon.getBonnummer());
	}

	@Test
	public void testGetLoon() {
		assertEquals(new Geld(0), this.bon.getLoon());
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(this.prijs, this.bon.getMateriaal());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		MutatiesBon mb = new MutatiesBon(this.omschrijving + ".", this.bonnummer, this.prijs);
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherBonnummer() {
		MutatiesBon mb = new MutatiesBon(this.omschrijving, this.bonnummer + ".", this.prijs);
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherPrijs() {
		MutatiesBon mb = new MutatiesBon(this.omschrijving, this.bonnummer, this.prijs.multiply(2));
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testToString() {
		assertEquals("<MutatiesBon[omschrijving, bonnummer, <Geld[10,00]>]>", this.bon.toString());
	}
}
