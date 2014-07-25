package org.rekeningsysteem.test.io.xml.adapter.particulier;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.particulier.EsselinkArtikelAdapter;

public class EsselinkArtikelAdapterTest {

	private EsselinkArtikelAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new EsselinkArtikelAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		EsselinkArtikel expected = new EsselinkArtikel("nr", "omschr", 12, "eenheid", new Geld(12));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
