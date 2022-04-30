package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

public class ReparatiesFactuurTest extends AbstractFactuurTest<ReparatiesInkoopOrder> {

	private ReparatiesFactuur factuur;

	@Override
	protected ReparatiesFactuur makeInstance() {
		return (ReparatiesFactuur) super.makeInstance();
	}

	@Override
	protected AbstractFactuur<ReparatiesInkoopOrder> makeInstance(FactuurHeader header, ItemList<ReparatiesInkoopOrder> itemList) {
		return new ReparatiesFactuur(header, itemList);
	}

	@Override
	protected ReparatiesFactuur makeNotInstance() {
		return (ReparatiesFactuur) super.makeNotInstance();
	}

	@Override
	protected AbstractFactuur<ReparatiesInkoopOrder> makeNotInstance(FactuurHeader otherHeader, ItemList<ReparatiesInkoopOrder> itemList) {
		return new ReparatiesFactuur(otherHeader, itemList);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.factuur = this.makeInstance();
	}

	@Test
	public void testToString() {
		String expected = "<ReparatiesFactuur[FactuurHeader[debiteur=Debiteur[debiteurID=Optional.empty, " 
			+ "naam=a, straat=b, nummer=c, postcode=d, plaats=e, btwNummer=Optional.empty], datum=1992-07-30, factuurnummer=Optional.empty], itemList]>";
		assertEquals(expected, this.factuur.toString());
	}
}
