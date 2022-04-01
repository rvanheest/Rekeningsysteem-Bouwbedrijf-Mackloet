package org.rekeningsysteem.test.data.offerte;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.data.util.visitor.RekeningVoidVisitor;
import org.rekeningsysteem.test.data.util.AbstractRekeningTest;

@RunWith(MockitoJUnitRunner.class)
public final class OfferteTest extends AbstractRekeningTest {

	private Offerte offerte;
	private final String tekst = "Lorem ipsum dolor sit amet.";
	@Mock private RekeningVisitor<Object> visitor;
	@Mock private RekeningVoidVisitor voidVisitor;

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
	public void testAccept() throws Exception {
		this.offerte.accept(this.visitor);

		verify(this.visitor).visit(eq(this.offerte));
	}

	@Test
	public void testAcceptVoid() throws Exception {
		this.offerte.accept(this.voidVisitor);

		verify(this.voidVisitor).visit(eq(this.offerte));
	}

	@Test
	public void testEqualsFalseOtherTekst() {
		Offerte off = new Offerte(this.offerte.getFactuurHeader(), this.tekst + ".", true);
		assertFalse(this.offerte.equals(off));
	}

	@Test
	public void testEqualsFalseOtherOndertekenen() {
		Offerte off = new Offerte(this.offerte.getFactuurHeader(), this.tekst, false);
		assertFalse(this.offerte.equals(off));
	}

	@Test
	public void testToString() {
		assertEquals("<Offerte[<FactuurHeader[<Debiteur[Optional.empty, a, b, c, d, e, "
				+ "Optional.empty]>, 1992-07-30, Optional.empty]>, "
				+ "Lorem ipsum dolor sit amet., true]>",
				this.offerte.toString());
	}
}
