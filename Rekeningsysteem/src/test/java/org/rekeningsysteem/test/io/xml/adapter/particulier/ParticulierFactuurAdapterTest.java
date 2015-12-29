package org.rekeningsysteem.test.io.xml.adapter.particulier;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierFactuurAdapter;

@Deprecated
public class ParticulierFactuurAdapterTest {

	private ParticulierFactuurAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ParticulierFactuurAdapter();
	}

	@Test
	public void testUnmarshalMarshal() {
		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("nr", "omschr", 1, "eenheid",
				new Geld(2)), 12.3, 21));
		itemList.add(new AnderArtikel("omschr", new Geld(1), 21));

		ItemList<AbstractLoon> loonList = new ItemList<>();
		loonList.add(new ProductLoon("productLoon", 12, new Geld(1), 6));

		ParticulierFactuur expected = new ParticulierFactuur(new OmschrFactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g", "h"),
				Currency.getInstance("EUR"), itemList, loonList);

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
