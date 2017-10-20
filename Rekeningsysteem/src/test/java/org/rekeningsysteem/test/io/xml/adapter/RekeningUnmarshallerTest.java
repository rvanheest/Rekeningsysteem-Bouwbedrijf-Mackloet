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
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesFactuurAdaptee;
import org.rekeningsysteem.io.xml.adapter.RekeningUnmarshaller;

public class RekeningUnmarshallerTest {

	private RekeningUnmarshaller visitor;

	@Before
	public void setUp() {
		this.visitor = new RekeningUnmarshaller();
	}

	@Test
	public void testVisitMutaratiesFactuur() {
		FactuurHeader header = new FactuurHeader(new Debiteur("a", "b", "c", "d", "e", "f"),
				LocalDate.now(), "g");
		Currency currency = Currency.getInstance("EUR");
		ItemList<MutatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new MutatiesInkoopOrder("omschr", "nr", new Geld(1)));
		MutatiesFactuur factuur = new MutatiesFactuur(header, currency, itemList);

		MutatiesFactuurAdaptee adaptee = MutatiesFactuurAdaptee.build(a -> a
        		.setFactuurHeader(header)
        		.setCurrency(currency)
        		.setList(itemList));

		assertEquals(factuur, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitOfferte() {
		FactuurHeader header = new FactuurHeader(
				new Debiteur("a", "b", "c", "d", "e", "f"), LocalDate.now(), "g");
		Offerte offerte = new Offerte(header, "h", true);

		OfferteAdaptee adaptee = OfferteAdaptee.build(a -> a
        		.setFactuurHeader(header)
        		.setTekst("h")
        		.setOndertekenen(true));

		assertEquals(offerte, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitParticulierFactuur() {
		OmschrFactuurHeader header = new OmschrFactuurHeader(new Debiteur("a", "b", "c", "d", "e",
				"f"), LocalDate.now(), "g", "h");
		Currency currency = Currency.getInstance("EUR");
		ItemList<ParticulierArtikel> itemList = new ItemList<>();
		itemList.add(new GebruiktEsselinkArtikel(new EsselinkArtikel("nr", "omschr", 1, "eenheid",
				new Geld(2)), 12.3, 21));
		itemList.add(new AnderArtikel("omschr", new Geld(1), 21));
		itemList.add(new ProductLoon("productLoon", 12, new Geld(1), 6));
		ParticulierFactuur factuur = new ParticulierFactuur(header, currency, itemList);

		ParticulierFactuurAdaptee adaptee = ParticulierFactuurAdaptee.build(a -> a
        		.setFactuurHeader(header)
        		.setCurrency(currency)
        		.setList(itemList));

		assertEquals(factuur, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitReparatiesFactuur() {
		FactuurHeader header = new FactuurHeader(new Debiteur("a", "b", "c", "d", "e", "f"),
				LocalDate.now(), "g");
		Currency currency = Currency.getInstance("EUR");
		ItemList<ReparatiesInkoopOrder> itemList = new ItemList<>();
		itemList.add(new ReparatiesInkoopOrder("omschr", "ordernummer", new Geld(1), new Geld(3)));
		ReparatiesFactuur factuur = new ReparatiesFactuur(header, currency, itemList);

		ReparatiesFactuurAdaptee adaptee = ReparatiesFactuurAdaptee.build(a -> a
        		.setFactuurHeader(header)
        		.setCurrency(currency)
        		.setList(itemList));

		assertEquals(factuur, this.visitor.visit(adaptee));
	}
}
