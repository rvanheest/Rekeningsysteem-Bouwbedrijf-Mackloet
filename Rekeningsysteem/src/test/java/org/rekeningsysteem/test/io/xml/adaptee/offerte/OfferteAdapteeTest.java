package org.rekeningsysteem.test.io.xml.adaptee.offerte;

import static org.junit.Assert.assertEquals;
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

	private final FactuurHeader header = new FactuurHeader(new Debiteur("", "", "", "", "", ""),
			LocalDate.now(), "");
	private final String text = "foobar";
	private final boolean ondertekenen = true;
	@Mock private RekeningAdapteeVisitor<Object> visitor;

	@Override
	protected OfferteAdaptee makeInstance() {
		return OfferteAdaptee.build(a -> a
				.setFactuurHeader(this.header)
				.setTekst(this.text)
				.setOndertekenen(this.ondertekenen));
	}

	@Override
	protected OfferteAdaptee getInstance() {
		return (OfferteAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetFactuurHeader() {
		assertEquals(this.header, this.getInstance().getFactuurHeader());
	}

	@Test
	public void testSetGetTekst() {
		assertEquals(this.text, this.getInstance().getTekst());
	}

	@Test
	public void testSetGetOndertekenen() {
		assertEquals(this.ondertekenen, this.getInstance().getOndertekenen());
	}

	@Test
	public void testAccept() {
		this.getInstance().accept(this.visitor);

		verify(this.visitor).visit(eq(this.getInstance()));
	}
}
