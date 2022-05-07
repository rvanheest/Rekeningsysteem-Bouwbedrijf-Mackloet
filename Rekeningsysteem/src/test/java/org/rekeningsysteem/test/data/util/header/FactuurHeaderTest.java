package org.rekeningsysteem.test.data.util.header;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public class FactuurHeaderTest {

	private FactuurHeader header;
	private final Debiteur debiteur = new Debiteur("RvH", "PB", "116", "3241TA", "MH");
	private final LocalDate datum = LocalDate.now();

	@Before
	public void setUp() {
		this.header = new FactuurHeader(this.debiteur, this.datum, "32013");
	}

	@Test
	public void testSecondConstructor() {
		this.header = new FactuurHeader(this.debiteur, this.datum);

		assertEquals(this.debiteur, this.header.debiteur());
		assertEquals(this.datum, this.header.datum());
		assertEquals(Optional.empty(), this.header.factuurnummer());
	}
}
