package org.rekeningsysteem.test.io.xml.root;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.root.MutatiesFactuurRoot;

public class MutatiesFactuurRootTest {

	private MutatiesFactuur expected;
	private MutatiesFactuurRoot root;

	@Before
	public void setUp() {
		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("omschr", "nr", new Geld(1)));
		this.expected = new MutatiesFactuur(new FactuurHeader(new Debiteur("a", "b",
				"c", "d", "e", "f"), LocalDate.now(), "g"), Currency.getInstance("EUR"), itemList);

		this.root = MutatiesFactuurRoot.build(a -> a.setRekening(this.expected));
	}

	@Test
	public void testGetType() {
		assertEquals("MutatiesFactuur", this.root.getType());
	}

	@Test
	public void testUnmarshalMarshal() {
		assertEquals(this.expected, this.root.getRekening());
	}
}
