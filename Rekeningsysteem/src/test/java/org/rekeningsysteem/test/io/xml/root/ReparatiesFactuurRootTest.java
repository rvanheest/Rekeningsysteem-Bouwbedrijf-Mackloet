package org.rekeningsysteem.test.io.xml.root;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.root.ReparatiesFactuurRoot;

public class ReparatiesFactuurRootTest {

	private ReparatiesFactuurRoot root;

	@Before
	public void setUp() {
		this.root = new ReparatiesFactuurRoot();
	}
	
	@Test
	public void testGetType() {
		assertEquals("ReparatiesFactuur", this.root.getType());
	}

	@Test
	public void testUnmarshalMarshal() {
		ItemList<ReparatiesBon> itemList = new ItemList<>();
		itemList.add(new ReparatiesBon("omschr", "bonnummer", new Geld(1), new Geld(3)));
		ReparatiesFactuur expected = new ReparatiesFactuur(new FactuurHeader(new Debiteur("a", "b",
				"c", "d", "e", "f"), LocalDate.now(), "g"), "i", itemList,
				new BtwPercentage(6, 21));
		this.root.setRekening(expected);

		assertEquals(expected, this.root.getRekening());
	}
}
