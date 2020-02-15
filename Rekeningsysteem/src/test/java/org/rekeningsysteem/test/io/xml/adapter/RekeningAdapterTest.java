package org.rekeningsysteem.test.io.xml.adapter;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.RekeningAdapter;

public class RekeningAdapterTest {

	private RekeningAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new RekeningAdapter();
	}

	@Test
	public void testMarshalUnmarshalMutaratiesFactuur() throws Exception {
		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("omschr", "nr", new Geld(1)));

		MutatiesFactuur expected = new MutatiesFactuur(new FactuurHeader(new Debiteur("a", "b",
				"c", "d", "e", "f"), LocalDate.now(), "g"), Currency.getInstance("EUR"), itemList);

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalOfferte() throws Exception {
		Offerte expected = new Offerte(new FactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g"), "h", true);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalParticulierFactuur() throws Exception {
		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("nr", "omschr", 1, "eenheid",
				new Geld(2)), 12.3, new BtwPercentage(21, false)));
		itemList.add(new AnderArtikel("omschr", new Geld(1), new BtwPercentage(21, true)));
		itemList.add(new ProductLoon("productLoon", 12, new Geld(1), new BtwPercentage(6, false)));

		ParticulierFactuur expected = new ParticulierFactuur(new OmschrFactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g", "h"),
				Currency.getInstance("EUR"), itemList);

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testMarshalUnmarshalReparatiesFactuur() throws Exception {
		ItemList<ReparatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new ReparatiesInkoopOrder("omschr", "ordernummer", new Geld(1), new Geld(3)));

		ReparatiesFactuur expected = new ReparatiesFactuur(new FactuurHeader(new Debiteur("a", "b",
				"c", "d", "e", "f"), LocalDate.now(), "g"), Currency.getInstance("EUR"), itemList);

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
