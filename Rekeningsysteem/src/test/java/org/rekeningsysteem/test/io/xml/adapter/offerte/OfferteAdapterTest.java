package org.rekeningsysteem.test.io.xml.adapter.offerte;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adapter.offerte.OfferteAdapter;

public class OfferteAdapterTest {

	private OfferteAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new OfferteAdapter();
	}

	@Test
	public void testUnmarshalMarshal() {
		Offerte expected = new Offerte(new FactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g"), "h", true);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
