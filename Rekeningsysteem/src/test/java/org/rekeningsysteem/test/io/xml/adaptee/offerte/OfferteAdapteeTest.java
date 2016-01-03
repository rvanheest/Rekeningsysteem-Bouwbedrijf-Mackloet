package org.rekeningsysteem.test.io.xml.adaptee.offerte;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.RekeningAdapteeVisitableTest;

@RunWith(MockitoJUnitRunner.class)
public class OfferteAdapteeTest extends RekeningAdapteeVisitableTest {

	@Mock private RekeningAdapteeVisitor<Object> visitor;

	@Override
	protected OfferteAdaptee makeInstance() {
		return new OfferteAdaptee();
	}

	@Override
	protected OfferteAdaptee getInstance() {
		return (OfferteAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetFactuurHeader() {
		FactuurHeader header = new FactuurHeader(new Debiteur("", "", "", "", "", ""),
				LocalDate.now(), "");
		this.getInstance().setFactuurHeader(header);
		assertEquals(header, this.getInstance().getFactuurHeader());
	}

	@Test
	public void testSetGetTekst() {
		this.getInstance().setTekst("foobar");
		assertEquals("foobar", this.getInstance().getTekst());
	}

	@Test
	public void testSetGetOndertekenen() {
		this.getInstance().setOndertekenen(true);
		assertTrue(this.getInstance().getOndertekenen());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}
