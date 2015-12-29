package org.rekeningsysteem.test.io.xml.adapter.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierItemListAdapter;

public class ParticulierItemListAdapterTest {

	private ParticulierItemListAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ParticulierItemListAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		ItemList<ParticulierArtikel> expected = new ItemList<>();
		expected.add(new AnderArtikel("omschr", new Geld(1.0), 21));
		expected.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("nr", "omschr", 1,
				"eenheid", new Geld(2)), 12.3, 6));
		expected.add(new InstantLoon("abc", new Geld(12.0), 9));
		expected.add(new ProductLoon("foobar", 2.0, new Geld(13.12), 15.0));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
