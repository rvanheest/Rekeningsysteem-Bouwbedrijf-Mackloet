package org.rekeningsysteem.test.io.xml.root;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.root.ParticulierFactuurRoot;

public class ParticulierFactuurRootTest {

	private ParticulierFactuur expected;
	private ParticulierFactuurRoot root;

	@Before
	public void setUp() {
		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("nr", "omschr", 1, "eenheid",
				new Geld(2)), 12.3, new BtwPercentage(21, false)));
		itemList.add(new AnderArtikel("omschr", new Geld(1), new BtwPercentage(21, true)));
		itemList.add(new ProductLoon("", 12, new Geld(1), new BtwPercentage(6, false)));
		this.expected = new ParticulierFactuur(new OmschrFactuurHeader(new Debiteur("a", "b", "c",
				"d", "e", "f"), LocalDate.now(), "g", "h"), Currency.getInstance("EUR"), itemList);

		this.root = ParticulierFactuurRoot.build(a -> a.setRekening(this.expected));
	}

	@Test
	public void testGetType() {
		assertEquals("ParticulierFactuur", this.root.getType());
	}

	@Test
	public void testUnmarshalMarshal() {
		assertEquals(this.expected, this.root.getRekening());
	}
}
