package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.test.data.util.AbstractFactuurTest;

public class MutatiesFactuurTest extends AbstractFactuurTest<MutatiesInkoopOrder> {

	private MutatiesFactuur factuur;

	@Override
	protected MutatiesFactuur makeInstance() {
		return (MutatiesFactuur) super.makeInstance();
	}

	@Override
	protected MutatiesFactuur makeInstance(FactuurHeader header, ItemList<MutatiesInkoopOrder> itemList) {
		return new MutatiesFactuur(header, itemList);
	}

	@Override
	protected MutatiesFactuur makeNotInstance() {
		return (MutatiesFactuur) super.makeNotInstance();
	}

	@Override
	protected AbstractFactuur<MutatiesInkoopOrder> makeNotInstance(FactuurHeader otherHeader, ItemList<MutatiesInkoopOrder> itemList) {
		return new MutatiesFactuur(otherHeader, itemList);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.factuur = this.makeInstance();
	}

	@Test
	public void testToString() {
		String expected = "<MutatiesFactuur[FactuurHeader[debiteur=Debiteur[debiteurID=Optional.empty, naam=a, straat=b, " 
			+ "nummer=c, postcode=d, plaats=e, btwNummer=Optional.empty], datum=1992-07-30, factuurnummer=Optional.empty], itemList]>";
		assertEquals(expected, this.factuur.toString());
	}
}
