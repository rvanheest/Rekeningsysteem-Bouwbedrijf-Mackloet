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

	private OfferteRoot root;

	@Before
	public void setUp() {
		this.root = new OfferteRoot();
	}
	
	@Test
	public void testGetType() {
		assertEquals("Offerte", this.root.getType());
	}

	@Test
	public void testUnmarshalMarshal() {
		Offerte expected = new Offerte(new FactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g"), "h", true);
		this.root.setRekening(expected);
		assertEquals(expected, this.root.getRekening());
	}
}
