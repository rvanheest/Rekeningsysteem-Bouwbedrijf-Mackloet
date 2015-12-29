package org.rekeningsysteem.test.io.xml.root;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.ParticulierFactuur2;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.root.ParticulierFactuur2Root;

public class ParticulierFactuur2RootTest {

	private ParticulierFactuur2Root root;

	@Before
	public void setUp() {
		this.root = new ParticulierFactuur2Root();
	}
	
	@Test
	public void testGetType() {
		assertEquals("ParticulierFactuur", this.root.getType());
	}

	@Test
	public void testUnmarshalMarshal() {
		ItemList<ParticulierArtikel2> itemList = new ItemList<>();
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("nr", "omschr", 1, "eenheid",
				new Geld(2)), 12.3, 21));
		itemList.add(new ParticulierArtikel2Impl("omschr", new Geld(1), 21));
		itemList.add(new ProductLoon2("", 12, new Geld(1), 6));

		ParticulierFactuur2 expected = new ParticulierFactuur2(new OmschrFactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g", "h"), Currency.getInstance("EUR"),
				itemList);
		this.root.setRekening(expected);

		assertEquals(expected, this.root.getRekening());
	}
}
