package org.rekeningsysteem.test.data.util.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class DebiteurTest extends EqualsHashCodeTest {

	private Debiteur debiteur;
	private final String naam = "Richard van Heest";
	private final String straat = "Prins Bernhardlaan";
	private final String nummer = "116";
	private final String postcode = "3241TA";
	private final String plaats = "Middelharnis";
	private final String btwNummer = "31071992";

	@Override
	protected Debiteur makeInstance() {
		return new Debiteur(this.naam, this.straat, this.nummer, this.postcode, this.plaats,
				this.btwNummer);
	}

	@Override
	protected Debiteur makeNotInstance() {
		return new Debiteur(this.naam + ".", this.straat, this.nummer, this.postcode, this.plaats,
				this.btwNummer);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.debiteur = this.makeInstance();
	}

	@Test
	public void testGetNaam() {
		assertEquals(this.naam, this.debiteur.getNaam());
	}

	@Test
	public void testGetStraat() {
		assertEquals(this.straat, this.debiteur.getStraat());
	}

	@Test
	public void testGetNummer() {
		assertEquals(this.nummer, this.debiteur.getNummer());
	}

	@Test
	public void testGetPostcode() {
		assertEquals(this.postcode, this.debiteur.getPostcode());
	}

	@Test
	public void testGetPlaats() {
		assertEquals(this.plaats, this.debiteur.getPlaats());
	}

	@Test
	public void testGetBtwNummer() {
		assertEquals(Optional.of(this.btwNummer), this.debiteur.getBtwNummer());
	}

	@Test
	public void testGetBtwNummerEmpty() {
		assertEquals(Optional.empty(), new Debiteur(this.naam, this.straat, this.nummer,
				this.postcode, this.plaats).getBtwNummer());
	}

	@Test
	public void testEqualsFalseOtherNaam() {
		Debiteur deb2 = new Debiteur(".", this.straat, this.nummer, this.postcode, this.plaats,
				this.btwNummer);
		assertFalse(this.debiteur.equals(deb2));
	}

	@Test
	public void testEqualsFalseOtherStraat() {
		Debiteur deb2 = new Debiteur(this.naam, ".", this.nummer, this.postcode, this.plaats,
				this.btwNummer);
		assertFalse(this.debiteur.equals(deb2));
	}

	@Test
	public void testEqualsFalseOtherNummer() {
		Debiteur deb2 = new Debiteur(this.naam, this.straat, ".", this.postcode, this.plaats,
				this.btwNummer);
		assertFalse(this.debiteur.equals(deb2));
	}

	@Test
	public void testEqualsFalseOtherPostcode() {
		Debiteur deb2 = new Debiteur(this.naam, this.straat, this.nummer, ".", this.plaats,
				this.btwNummer);
		assertFalse(this.debiteur.equals(deb2));
	}

	@Test
	public void testEqualsFalseOtherPlaats() {
		Debiteur deb2 = new Debiteur(this.naam, this.straat, this.nummer, this.postcode, ".",
				this.btwNummer);
		assertFalse(this.debiteur.equals(deb2));
	}

	@Test
	public void testEqualsFalseOtherBtwNummer() {
		Debiteur deb2 = new Debiteur(this.naam, this.straat, this.nummer, this.postcode,
				this.plaats, ".");
		assertFalse(this.debiteur.equals(deb2));
	}

	@Test
	public void testToString() {
		assertEquals("<Debiteur[Richard van Heest, Prins Bernhardlaan, 116, 3241TA, "
				+ "Middelharnis, Optional[31071992]]>", this.debiteur.toString());
	}
}
