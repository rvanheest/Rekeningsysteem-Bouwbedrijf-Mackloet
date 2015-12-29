package org.rekeningsysteem.test.io.xml.adapter.particulier2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.particulier2.ParticulierArtikel2Adapter;

public class ParticulierArtikel2AdapterTest {

	private ParticulierArtikel2Adapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ParticulierArtikel2Adapter();
	}

	@Test
	public void testMarshalUnmarshalAnderArtikel() {
		ParticulierArtikel2Impl expected = new ParticulierArtikel2Impl("omschr", new Geld(1), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalGebruiktEsselinkArtikel() {
		EsselinkParticulierArtikel expected = new EsselinkParticulierArtikel(new EsselinkArtikel("nr",
				"omschr", 1, "eenheid", new Geld(2)), 12.3, 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testInstantLoonMarshalUnmarshal() {
		InstantLoon2 expected = new InstantLoon2("omschr", new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testProductLoonMarshalUnmarshal() {
		ProductLoon2 expected = new ProductLoon2("omschr", 3.0, new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
