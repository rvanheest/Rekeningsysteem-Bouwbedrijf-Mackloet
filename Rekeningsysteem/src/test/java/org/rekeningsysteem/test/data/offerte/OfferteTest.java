package org.rekeningsysteem.test.data.offerte;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.test.data.util.AbstractRekeningTest;

public final class OfferteTest extends AbstractRekeningTest {

	private final String tekst = "Lorem ipsum dolor sit amet.";

	@Override
	protected Offerte makeInstance() {
		return new Offerte(this.getTestFactuurHeader(), this.tekst, true);
	}

	@Override
	protected Offerte makeNotInstance() {
		return new Offerte(this.getTestFactuurHeader(), this.tekst, false);
	}

	@Override
	protected Offerte getInstance() {
		return (Offerte) super.getInstance();
	}

	@Test
	public void testGetTekst() {
		assertEquals(this.tekst, this.getInstance().getTekst());
	}

	@Test
	public void testSetTekst() {
		String temp = "Lorem ipsum dolor sit amet, consectetur.";
		this.getInstance().setTekst(temp);
		assertEquals(temp, this.getInstance().getTekst());
	}

	@Test
	public void testIsOndertekenen() {
		assertTrue(this.getInstance().isOndertekenen());
	}

	@Test
	public void testSetOndertekenen() {
		this.getInstance().setOndertekenen(false);
		assertFalse(this.getInstance().isOndertekenen());
	}

	@Test
	public void testAccept() throws Exception {
		this.getInstance().accept(this.getMockedVisitor());

		verify(this.getMockedVisitor()).visit(eq(this.getInstance()));
	}

	@Test
	public void testEqualsFalseOtherTekst() {
		Offerte off = new Offerte(this.getTestFactuurHeader(), this.tekst + ".", true);
		assertFalse(this.getInstance().equals(off));
	}

	@Test
	public void testEqualsFalseOtherOndertekenen() {
		Offerte off = new Offerte(this.getTestFactuurHeader(), this.tekst, false);
		assertFalse(this.getInstance().equals(off));
	}

	@Test
	public void testToString() {
		assertEquals("<Offerte[<FactuurHeader[<Debiteur[a, b, c, d, e, Optional.empty]>, "
				+ "<Datum[30-07-1992]>, Optional[f]]>, Lorem ipsum dolor sit amet., true]>",
				this.getInstance().toString());
	}
}
