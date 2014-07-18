package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.Datum;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.exception.DatumException;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractRekeningTest extends EqualsHashCodeTest {

	private AbstractRekening rekening;
	private FactuurHeader header;
	@Mock private RekeningVisitor visitor;

	@Override
	protected abstract AbstractRekening makeInstance();

	protected AbstractRekening getInstance() {
		return this.rekening;
	}

	protected FactuurHeader getTestFactuurHeader() {
		return this.header;
	}

	protected RekeningVisitor getMockedVisitor() {
		return this.visitor;
	}

	@Override
	@Before
	public void setUp() {
		try {
			super.setUp();
			this.header = new FactuurHeader(new Debiteur("a", "b", "c", "d", "e"),
					new Datum(30, 07, 1992), "f");
			this.rekening = this.makeInstance();
		}
		catch (DatumException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetHeader() {
		assertEquals(this.header, this.rekening.getFactuurHeader());
	}

	@Test
	public void testEqualsFalseOtherFactuurHeader() {
		this.header = new FactuurHeader(new Debiteur("", "", "", "", ""), new Datum(), "test");
		assertFalse(this.rekening.equals(this.makeInstance()));
	}
}