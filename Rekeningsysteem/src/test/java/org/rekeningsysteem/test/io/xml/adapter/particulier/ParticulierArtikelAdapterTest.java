package org.rekeningsysteem.test.io.xml.adapter.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierArtikelAdapter;

public class ParticulierArtikelAdapterTest {

	private ParticulierArtikelAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ParticulierArtikelAdapter();
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
	public void testInstantLoonMarshalUnmarshal() {
		InstantLoon expected = new InstantLoon("omschr", new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testProductLoonMarshalUnmarshal() {
		ProductLoon expected = new ProductLoon("omschr", 3.0, new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
