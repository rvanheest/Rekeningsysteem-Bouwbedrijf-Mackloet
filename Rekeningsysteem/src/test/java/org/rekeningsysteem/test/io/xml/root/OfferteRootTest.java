package org.rekeningsysteem.test.io.xml.root;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.root.OfferteRoot;

public class OfferteRootTest {

	private Offerte expected;
	private OfferteRoot root;

	@Before
	public void setUp() {
		this.expected = new Offerte(new FactuurHeader(new Debiteur("a", "b", "c", "d", "e", "f"),
				LocalDate.now(), "g"), "h", true);

		this.root = OfferteRoot.build(a -> a.setRekening(this.expected));
	}

	@Test
	public void testGetType() {
		assertEquals("Offerte", this.root.getType());
	}

	@Test
	public void testUnmarshalMarshal() {
		assertEquals(this.expected, this.root.getRekening());
	}
}
