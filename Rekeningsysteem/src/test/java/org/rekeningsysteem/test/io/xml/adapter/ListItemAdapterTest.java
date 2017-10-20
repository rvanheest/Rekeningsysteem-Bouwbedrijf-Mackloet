package org.rekeningsysteem.test.io.xml.adapter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.ListItemAdapter;

public class ListItemAdapterTest {

	private ListItemAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ListItemAdapter();
	}

	@Test
	public void testMarshalUnmarshalAnderArtikel() {
		AnderArtikel expected = new AnderArtikel("omschr", new Geld(1), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalGebruiktEsselinkArtikel() {
		GebruiktEsselinkArtikel expected = new GebruiktEsselinkArtikel(new EsselinkArtikel("nr",
				"omschr", 1, "eenheid", new Geld(2)), 12.3, 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalInstantLoon() {
		InstantLoon expected = new InstantLoon("omschr", new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalProductLoon() {
		ProductLoon expected = new ProductLoon("omschr", 3.0, new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalMutatiesInkoopOrder() {
		MutatiesInkoopOrder expected = new MutatiesInkoopOrder("omschr", "nr", new Geld(1));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalReparatiesInkoopOrder() {
		ReparatiesInkoopOrder expected = new ReparatiesInkoopOrder("omschr", "ordernummer", new Geld(1), new Geld(3));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
