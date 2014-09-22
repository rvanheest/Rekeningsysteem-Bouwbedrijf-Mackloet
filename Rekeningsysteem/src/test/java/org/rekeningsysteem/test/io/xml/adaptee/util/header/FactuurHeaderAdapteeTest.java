package org.rekeningsysteem.test.io.xml.adaptee.util.header;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.xml.adaptee.util.header.FactuurHeaderAdaptee;

public class FactuurHeaderAdapteeTest {

	private FactuurHeaderAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new FactuurHeaderAdaptee();
	}

	@Test
	public void testSetGetDebiteur() {
		Debiteur debiteur = new Debiteur("a", "b", "c", "d", "e", "f");
		this.adaptee.setDebiteur(debiteur);
		assertEquals(debiteur, this.adaptee.getDebiteur());
	}

	@Test
	public void testSetGetDatum() {
		LocalDate datum = LocalDate.of(1992, 7, 30);
		this.adaptee.setDatum(datum);
		assertEquals(datum, this.adaptee.getDatum());
	}

	@Test
	public void testSetGetFactuurnummer() {
		this.adaptee.setFactuurnummer("abcd");
		assertEquals("abcd", this.adaptee.getFactuurnummer());
	}
}