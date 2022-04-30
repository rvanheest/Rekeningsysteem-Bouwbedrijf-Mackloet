package org.rekeningsysteem.test.data.offerte;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.test.data.util.AbstractRekeningTest;

public final class OfferteTest extends AbstractRekeningTest {

	private Offerte offerte;
	private final String tekst = "Lorem ipsum dolor sit amet.";

	@Override
	protected Offerte makeInstance() {
		return (Offerte) super.makeInstance();
	}

	@Override
	protected Offerte makeInstance(FactuurHeader header) {
		return new Offerte(header, this.tekst, true);
	}

	@Override
	protected Offerte makeNotInstance() {
		return (Offerte) super.makeNotInstance();
	}

	@Override
	protected AbstractRekening makeNotInstance(FactuurHeader otherHeader) {
		return new Offerte(otherHeader, this.tekst, true);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.offerte = this.makeInstance();
	}

	@Test
	public void testGetTekst() {
		assertEquals(this.tekst, this.offerte.getTekst());
	}

	@Test
	public void testIsOndertekenen() {
		assertTrue(this.offerte.isOndertekenen());
	}

	@Test
	public void testEqualsFalseOtherTekst() {
		Offerte off = new Offerte(this.offerte.getFactuurHeader(), this.tekst + ".", true);
		assertNotEquals(this.offerte, off);
	}

	@Test
	public void testEqualsFalseOtherOndertekenen() {
		Offerte off = new Offerte(this.offerte.getFactuurHeader(), this.tekst, false);
		assertNotEquals(this.offerte, off);
	}

	@Test
	public void testToString() {
		assertEquals("<Offerte[FactuurHeader[debiteur=Debiteur[debiteurID=Optional.empty, naam=a, " 
				+ "straat=b, nummer=c, postcode=d, plaats=e, btwNummer=Optional.empty], datum=1992-07-30, factuurnummer=Optional.empty], "
				+ "Lorem ipsum dolor sit amet., true]>",
				this.offerte.toString());
	}
}
