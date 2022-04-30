package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

public abstract class AbstractRekeningTest extends EqualsHashCodeTest {

	private AbstractRekening rekening;
	private final FactuurHeader header = new FactuurHeader(new Debiteur("a", "b", "c", "d", "e"),
			LocalDate.of(1992, 7, 30));

	@Override
	protected AbstractRekening makeInstance() {
		return this.makeInstance(this.header);
	}

	protected abstract AbstractRekening makeInstance(FactuurHeader header);

	@Override
	protected AbstractRekening makeNotInstance() {
		return this.makeNotInstance(new FactuurHeader(new Debiteur("", "", "", "", ""),
				LocalDate.of(1992, 7, 30)));
	}

	protected abstract AbstractRekening makeNotInstance(FactuurHeader otherHeader);

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.rekening = this.makeInstance();
	}

	@Test
	public void testGetHeader() {
		assertEquals(this.header, this.rekening.getFactuurHeader());
	}

	@Test
	public void testEqualsFalseOtherFactuurHeader() {
		FactuurHeader header2 = new FactuurHeader(new Debiteur("", "", "", "", ""),
				LocalDate.now(), "test");
		AbstractRekening rekening2 = this.makeNotInstance(header2);
		assertFalse(this.rekening.equals(rekening2));
	}
}
