package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

public class MutatiesFactuurTest extends AbstractFactuurTest<MutatiesBon> {

	private MutatiesFactuur factuur;
	@Mock private RekeningVisitor visitor;

	@Override
	protected MutatiesFactuur makeInstance() {
		return (MutatiesFactuur) super.makeInstance();
	}

	@Override
	protected MutatiesFactuur makeInstance(FactuurHeader header, Currency currency,
			ItemList<MutatiesBon> itemList) {
		return new MutatiesFactuur(header, currency, itemList);
	}

	@Override
	protected MutatiesFactuur makeNotInstance() {
		return (MutatiesFactuur) super.makeNotInstance();
	}

	@Override
	protected AbstractFactuur<MutatiesBon> makeNotInstance(FactuurHeader otherHeader,
			Currency currency, ItemList<MutatiesBon> itemList) {
		return new MutatiesFactuur(otherHeader, currency, itemList);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.factuur = this.makeInstance();
	}

	@Test
	public void testAccept() throws Exception {
		this.factuur.accept(this.visitor);

		verify(this.visitor).visit(eq(this.factuur));
	}

	@Test
	public void testToString() {
		String expected = "<MutatiesFactuur[<FactuurHeader[<Debiteur[a, b, c, d, e, "
				+ "Optional.empty]>, 1992-07-30, Optional.empty]>, EUR, itemList]>";
		assertEquals(expected, this.factuur.toString());
	}
}
