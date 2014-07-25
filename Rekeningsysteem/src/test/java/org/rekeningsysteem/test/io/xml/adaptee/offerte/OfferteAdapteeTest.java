package org.rekeningsysteem.test.io.xml.adaptee.offerte;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;

public class OfferteAdapteeTest {

	private OfferteAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new OfferteAdaptee();
	}

	@Test
	public void testSetGetFactuurHeader() {
		FactuurHeader header = new FactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), "");
		this.adaptee.setFactuurHeader(header);
		assertEquals(header, this.adaptee.getFactuurHeader());
	}

	@Test
	public void testSetGetTekst() {
		this.adaptee.setTekst("foobar");
		assertEquals("foobar", this.adaptee.getTekst());
	}

	@Test
	public void testSetGetOndertekenen() {
		this.adaptee.setOndertekenen(true);
		assertTrue(this.adaptee.getOndertekenen());
	}
}
