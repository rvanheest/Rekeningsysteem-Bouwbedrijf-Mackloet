package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;
import org.rekeningsysteem.test.data.util.ListItemTest;

public class MutatiesBonTest extends EqualsHashCodeTest implements ListItemTest {

	private MutatiesBon bon;
	private final String omschrijving = "omschrijving";
	private final String bonnummer = "bonnummer";
	private final Geld prijs = new Geld(10);

	@Override
	protected Object makeInstance() {
		return new MutatiesBon(this.omschrijving, this.bonnummer, this.prijs);
	}

	@Override
	protected Object makeNotInstance() {
		return new MutatiesBon(this.omschrijving + ".", this.bonnummer, this.prijs);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.bon = new MutatiesBon(this.omschrijving, this.bonnummer, this.prijs);
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
	@Override
	public void testGetLoon() {
		assertEquals(new Geld(0), this.bon.getLoon());
	}

	@Test
	@Override
	public void testGetMateriaal() {
		assertEquals(this.prijs, this.bon.getMateriaal());
	}

	@Test
	@Override
	public void testGetTotaal() {
		assertEquals(this.prijs, this.bon.getTotaal());
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
		MutatiesBon mb = new MutatiesBon(this.omschrijving, this.bonnummer, new Geld(3));
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testToString() {
		assertEquals("<MutatiesBon[omschrijving, bonnummer, <Geld[10,00]>]>", this.bon.toString());
	}
}
