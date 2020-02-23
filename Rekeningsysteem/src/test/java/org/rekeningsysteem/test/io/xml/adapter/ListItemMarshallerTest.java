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
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesInkoopOrderAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesInkoopOrderAdaptee;
import org.rekeningsysteem.io.xml.adapter.ListItemMarshaller;

public class ListItemMarshallerTest {

	private ListItemMarshaller visitor;

	@Before
	public void setUp() {
		this.visitor = new ListItemMarshaller();
	}

	@Test
	public void testVisitMutatiesInkoopOrder() {
		MutatiesInkoopOrder order = new MutatiesInkoopOrder("omschr", "ordernummer", new Geld(12));
		MutatiesInkoopOrderAdaptee adaptee = this.visitor.visit(order);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals("ordernummer", adaptee.getBonnummer());
		assertEquals(new Geld(12), adaptee.getPrijs());
	}

	@Test
	public void testVisitGebruiktEsselinkArtikel() {
		EsselinkArtikel artikel = new EsselinkArtikel("artNr", "omschr", 1, "eenh", new Geld(12));
		GebruiktEsselinkArtikel gea = new GebruiktEsselinkArtikel(artikel, 2.3, new BtwPercentage(4.2, false));
		GebruiktEsselinkArtikelAdaptee adaptee = this.visitor.visit(gea);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(artikel, adaptee.getArtikel());
		assertEquals(2.3, adaptee.getAantal(), 0.0);
		assertEquals(new BtwPercentage(4.2, false), adaptee.getMateriaalBtwPercentage());
	}

	@Test
	public void testVisitAnderArtikel() {
		AnderArtikel artikel = new AnderArtikel("omschr", new Geld(12), new BtwPercentage(42.0, true));
		AnderArtikelAdaptee adaptee = this.visitor.visit(artikel);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(new Geld(12), adaptee.getPrijs());
		assertEquals(new BtwPercentage(42.0, true), adaptee.getMateriaalBtwPercentage());
	}

	@Test
	public void testVisitReparatiesInkoopOrder() {
		ReparatiesInkoopOrder order = new ReparatiesInkoopOrder("omschr", "ordernummer", new Geld(12), new Geld(1334));
		ReparatiesInkoopOrderAdaptee adaptee = this.visitor.visit(order);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals("ordernummer", adaptee.getBonnummer());
		assertEquals(new Geld(12), adaptee.getLoon());
		assertEquals(new Geld(1334), adaptee.getMateriaal());
	}

	@Test
	public void testVisitInstantLoon() {
		InstantLoon loon = new InstantLoon("omschr", new Geld(12), new BtwPercentage(21, true));
		InstantLoonAdaptee adaptee = this.visitor.visit(loon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(new Geld(12), adaptee.getLoon());
		assertEquals(new BtwPercentage(21.0, true), adaptee.getLoonBtwPercentage());
	}

	@Test
	public void testVisitProductLoon() {
		ProductLoon loon = new ProductLoon("omschr", 15, new Geld(12), new BtwPercentage(21, false));
		ProductLoonAdaptee adaptee = this.visitor.visit(loon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(15.0, adaptee.getUren(), 0.0);
		assertEquals(new Geld(12), adaptee.getUurloon());
		assertEquals(new BtwPercentage(21.0, false), adaptee.getLoonBtwPercentage());
	}
}
