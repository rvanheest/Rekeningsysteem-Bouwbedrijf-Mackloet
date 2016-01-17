package org.rekeningsysteem.test.io.xml.root;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;

public class ReparatiesFactuurRootTest {

	private ReparatiesFactuur expected;
	private ReparatiesFactuurRoot root;

	@Before
	public void setUp() {
		ItemList<ReparatiesBon> itemList = new ItemList<>();
		itemList.add(new ReparatiesBon("omschr", "bonnummer", new Geld(1), new Geld(3)));
		this.expected = new ReparatiesFactuur(new FactuurHeader(new Debiteur("a", "b", "c", "d",
				"e", "f"), LocalDate.now(), "g"), Currency.getInstance("EUR"), itemList);

		this.root = ReparatiesFactuurRoot.build(a -> a.setRekening(this.expected));
	}

	@Test
	public void testGetType() {
		assertEquals("ReparatiesFactuur", this.root.getType());
	}

	@Test
	public void testUnmarshalMarshal() {
		assertEquals(this.expected, this.root.getRekening());
	}
}
