package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

@RunWith(MockitoJUnitRunner.class)
public class ReparatiesFactuurTest extends AbstractFactuurTest<ReparatiesInkoopOrder> {

	private ReparatiesFactuur factuur;
	@Mock private RekeningVisitor<Object> visitor;

	@Override
	protected ReparatiesFactuur makeInstance() {
		return (ReparatiesFactuur) super.makeInstance();
	}

	@Override
	protected AbstractFactuur<ReparatiesInkoopOrder> makeInstance(FactuurHeader header, Currency currency,
			ItemList<ReparatiesInkoopOrder> itemList) {
		return new ReparatiesFactuur(header, currency, itemList);
	}

	@Override
	protected ReparatiesFactuur makeNotInstance() {
		return (ReparatiesFactuur) super.makeNotInstance();
	}

	@Override
	protected AbstractFactuur<ReparatiesInkoopOrder> makeNotInstance(FactuurHeader otherHeader,
			Currency currency, ItemList<ReparatiesInkoopOrder> itemList) {
		return new ReparatiesFactuur(otherHeader, currency, itemList);
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
		String expected = "<ReparatiesFactuur[FactuurHeader[debiteur=Debiteur[debiteurID=Optional.empty, " 
			+ "naam=a, straat=b, nummer=c, postcode=d, plaats=e, btwNummer=Optional.empty], datum=1992-07-30, factuurnummer=Optional.empty], EUR, itemList]>";
		assertEquals(expected, this.factuur.toString());
	}
}
