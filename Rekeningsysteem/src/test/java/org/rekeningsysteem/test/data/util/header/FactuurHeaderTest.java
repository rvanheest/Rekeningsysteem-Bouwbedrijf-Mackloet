package org.rekeningsysteem.test.data.util.header;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.exception.DatumException;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public class FactuurHeaderTest extends EqualsHashCodeTest {

	private FactuurHeader header;
	private final Debiteur debiteur = new Debiteur("RvH", "PB", "116", "3241TA", "MH");
	private final LocalDate datum = LocalDate.now();
	private final String factuurnummer = "32013";

	@Override
	protected FactuurHeader makeInstance() {
		return new FactuurHeader(this.debiteur, this.datum, this.factuurnummer);
	}

	@Override
	protected FactuurHeader makeNotInstance() {
		return new FactuurHeader(this.debiteur, this.datum, this.factuurnummer + ".");
	}

	protected FactuurHeader getInstance() {
		return this.header;
	}

	protected Debiteur getTestDebiteur() {
		return this.debiteur;
	}

	protected LocalDate getTestDatum() {
		return this.datum;
	}

	protected String getTestFatuurnummer() {
		return this.factuurnummer;
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.header = this.makeInstance();
	}

	@Test
	public void testGetDebiteur() {
		assertEquals(this.debiteur, this.header.getDebiteur());
	}

	@Test
	public void testSetDebiteur() {
		Debiteur deb2 = new Debiteur("", "", "", "", "", "");
		this.header.setDebiteur(deb2);
		assertEquals(deb2, this.header.getDebiteur());
	}

	@Test
	public void testGetDatum() {
		assertEquals(this.datum, this.header.getDatum());
	}

	@Test
	public void testSetDatum() throws DatumException {
		LocalDate datum2 = LocalDate.of(1992, 7, 30);
		this.header.setDatum(datum2);
		assertEquals(datum2, this.header.getDatum());
	}

	@Test
	public void testGetFactuurnummer() {
		assertEquals(Optional.of(this.factuurnummer), this.header.getFactuurnummer());
	}

	@Test
	public void testSetFactuurnummer() {
		this.header.setFactuurnummer("202020");
		assertEquals(Optional.of("202020"), this.header.getFactuurnummer());
	}

	@Test
	public void testSecondConstructor() {
		this.header = new FactuurHeader(this.debiteur, this.datum);

		assertEquals(this.debiteur, this.header.getDebiteur());
		assertEquals(this.datum, this.header.getDatum());
		assertEquals(Optional.empty(), this.header.getFactuurnummer());
	}

	@Test
	public void testEqualsFalseOtherDebiteur() {
		Debiteur debiteur2 = new Debiteur("RvH", "PB", "116", "3241TA", "MH", "20");
		assertFalse(this.header.equals(new FactuurHeader(debiteur2, this.datum,
				this.factuurnummer)));
	}

	@Test
	public void testEqualsFalseOtherDatum() throws DatumException {
		LocalDate datum2 = LocalDate.of(1992, 7, 31);
		assertFalse(this.header.equals(new FactuurHeader(this.debiteur, datum2,
				this.factuurnummer)));
	}

	@Test
	public void testEqualsFalseOtherFactuurnummer() {
		assertFalse(this.header.equals(new FactuurHeader(this.debiteur, this.datum,
				this.factuurnummer + ".")));
	}

	@Test
	public void testToString() {
		assertEquals("<FactuurHeader[<Debiteur[RvH, PB, 116, 3241TA, MH, Optional.empty]>, "
				+ this.datum.toString() + ", Optional[32013]]>", this.header.toString());
	}
}
