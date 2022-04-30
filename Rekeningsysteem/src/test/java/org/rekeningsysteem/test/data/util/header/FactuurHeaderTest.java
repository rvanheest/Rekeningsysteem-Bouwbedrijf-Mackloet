package org.rekeningsysteem.test.data.util.header;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
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

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.header = this.makeInstance();
	}

	@Test
	public void testSecondConstructor() {
		this.header = new FactuurHeader(this.debiteur, this.datum);

		assertEquals(this.debiteur, this.header.debiteur());
		assertEquals(this.datum, this.header.datum());
		assertEquals(Optional.empty(), this.header.factuurnummer());
	}
}
