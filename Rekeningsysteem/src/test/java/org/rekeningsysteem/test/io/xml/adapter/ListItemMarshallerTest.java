package org.rekeningsysteem.test.io.xml.adapter;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;
import org.rekeningsysteem.io.xml.adapter.ListItemMarshaller;

public class ListItemMarshallerTest {

	private ListItemMarshaller visitor;

	@Before
	public void setUp() {
		this.visitor = new ListItemMarshaller();
	}

	@Test
	public void testVisitMutatiesBon() {
		MutatiesBon bon = new MutatiesBon("omschr", "bonn", new Geld(12));
		MutatiesBonAdaptee adaptee = this.visitor.visit(bon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals("bonn", adaptee.getBonnummer());
		assertEquals(new Geld(12), adaptee.getPrijs());
	}

	@Test
	public void testVisitGebruiktEsselinkArtikel() {
		EsselinkArtikel artikel = new EsselinkArtikel("artNr", "omschr", 1, "eenh", new Geld(12));
		GebruiktEsselinkArtikel bon = new GebruiktEsselinkArtikel(artikel, 2.3, 4.2);
		GebruiktEsselinkArtikelAdaptee adaptee = this.visitor.visit(bon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(artikel, adaptee.getArtikel());
		assertEquals(2.3, adaptee.getAantal(), 0.0);
		assertEquals(4.2, adaptee.getMateriaalBtwPercentage(), 0.0);
	}

	@Test
	public void testVisitAnderArtikel() {
		AnderArtikel bon = new AnderArtikel("omschr", new Geld(12), 42.0);
		AnderArtikelAdaptee adaptee = this.visitor.visit(bon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(new Geld(12), adaptee.getPrijs());
		assertEquals(42.0, adaptee.getMateriaalBtwPercentage(), 0.0);
	}

	@Test
	public void testVisitReparatiesBon() {
		ReparatiesBon bon = new ReparatiesBon("omschr", "bonn", new Geld(12), new Geld(1334));
		ReparatiesBonAdaptee adaptee = this.visitor.visit(bon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals("bonn", adaptee.getBonnummer());
		assertEquals(new Geld(12), adaptee.getLoon());
		assertEquals(new Geld(1334), adaptee.getMateriaal());
	}

	@Test
	public void testVisitInstantLoon() {
		InstantLoon bon = new InstantLoon("omschr", new Geld(12), 21);
		InstantLoonAdaptee adaptee = this.visitor.visit(bon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(new Geld(12), adaptee.getLoon());
		assertEquals(21.0, adaptee.getLoonBtwPercentage(), 0.0);
	}

	@Test
	public void testVisitProductLoon() {
		ProductLoon bon = new ProductLoon("omschr", 15, new Geld(12), 21);
		ProductLoonAdaptee adaptee = this.visitor.visit(bon);

		assertEquals("omschr", adaptee.getOmschrijving());
		assertEquals(15.0, adaptee.getUren(), 0.0);
		assertEquals(new Geld(12), adaptee.getUurloon());
		assertEquals(21.0, adaptee.getLoonBtwPercentage(), 0.0);
	}
}