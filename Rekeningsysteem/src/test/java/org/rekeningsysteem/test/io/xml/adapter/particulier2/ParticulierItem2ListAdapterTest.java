package org.rekeningsysteem.test.io.xml.adapter.particulier2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier2.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.data.particulier2.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier2.loon.ProductLoon2;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adapter.particulier2.ParticulierItem2ListAdapter;

public class ParticulierItem2ListAdapterTest {

	private ParticulierItem2ListAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ParticulierItem2ListAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		ItemList<ParticulierArtikel2> expected = new ItemList<>();
		expected.add(new ParticulierArtikel2Impl("omschr", new Geld(1.0), 21));
		expected.add(new EsselinkParticulierArtikel(new EsselinkArtikel("nr", "omschr", 1,
				"eenheid", new Geld(2)), 12.3, 6));
		expected.add(new InstantLoon2("abc", new Geld(12.0), 9));
		expected.add(new ProductLoon2("foobar", 2.0, new Geld(13.12), 15.0));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
