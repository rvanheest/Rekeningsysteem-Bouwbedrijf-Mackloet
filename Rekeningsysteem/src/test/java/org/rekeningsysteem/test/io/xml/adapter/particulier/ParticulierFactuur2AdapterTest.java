package org.rekeningsysteem.test.io.xml.adapter.particulier;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.ParticulierFactuur2;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierFactuur2Adapter;

public class ParticulierFactuur2AdapterTest {

	private ParticulierFactuur2Adapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ParticulierFactuur2Adapter();
	}

	@Test
	public void testUnmarshalMarshal() {
		ItemList<ParticulierArtikel2> itemList = new ItemList<>();
		itemList.add(new EsselinkParticulierArtikel(new EsselinkArtikel("nr", "omschr", 1, "eenheid",
				new Geld(2)), 12.3, 21));
		itemList.add(new ParticulierArtikel2Impl("omschr", new Geld(1), 21));
		itemList.add(new ProductLoon2("productLoon", 12, new Geld(1), 6));

		ParticulierFactuur2 expected = new ParticulierFactuur2(new OmschrFactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g", "h"),
				Currency.getInstance("EUR"), itemList);

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
