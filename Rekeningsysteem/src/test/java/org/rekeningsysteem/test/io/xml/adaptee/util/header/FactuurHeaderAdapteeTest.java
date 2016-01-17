package org.rekeningsysteem.test.io.xml.adaptee.util.header;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.xml.adaptee.util.header.FactuurHeaderAdaptee;

public class FactuurHeaderAdapteeTest {

	private final Debiteur debiteur = new Debiteur("a", "b", "c", "d", "e", "f");
	private final LocalDate datum = LocalDate.of(1992, 7, 30);
	private final String factuurnummer = "abcd";
	private FactuurHeaderAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = FactuurHeaderAdaptee.build(a -> a
				.setDebiteur(this.debiteur)
				.setDatum(this.datum)
				.setFactuurnummer(this.factuurnummer));
	}

	@Test
	public void testSetGetDebiteur() {
		assertEquals(this.debiteur, this.adaptee.getDebiteur());
	}

	@Test
	public void testSetGetDatum() {
		assertEquals(this.datum, this.adaptee.getDatum());
	}

	@Test
	public void testSetGetFactuurnummer() {
		assertEquals(this.factuurnummer, this.adaptee.getFactuurnummer());
	}
}
