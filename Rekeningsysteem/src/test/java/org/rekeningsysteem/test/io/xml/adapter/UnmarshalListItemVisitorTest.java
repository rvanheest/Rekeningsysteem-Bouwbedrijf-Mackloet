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
import org.rekeningsysteem.io.xml.adapter.UnmarshalListItemVisitor;

public class UnmarshalListItemVisitorTest {

	private UnmarshalListItemVisitor visitor;

	@Before
	public void setUp() {
		this.visitor = new UnmarshalListItemVisitor();
	}

	@Test
	public void testVisitMutatiesBon() {
		MutatiesBonAdaptee adaptee = new MutatiesBonAdaptee();
		adaptee.setOmschrijving("omschr");
		adaptee.setBonnummer("bonn");
		adaptee.setPrijs(new Geld(12));
		MutatiesBon bon = new MutatiesBon("omschr", "bonn", new Geld(12));

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitGebruiktEsselinkArtikel() {
		EsselinkArtikel artikel = new EsselinkArtikel("artNr", "omschr", 1, "eenh", new Geld(12));
		GebruiktEsselinkArtikelAdaptee adaptee = new GebruiktEsselinkArtikelAdaptee();
		adaptee.setOmschrijving("omschr");
		adaptee.setArtikel(artikel);
		adaptee.setAantal(2.3);
		adaptee.setMateriaalBtwPercentage(4.2);
		GebruiktEsselinkArtikel bon = new GebruiktEsselinkArtikel(artikel, 2.3, 4.2);

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitAnderArtikel() {
		AnderArtikelAdaptee adaptee = new AnderArtikelAdaptee();
		adaptee.setOmschrijving("omschr");
		adaptee.setPrijs(new Geld(12));
		adaptee.setMateriaalBtwPercentage(42);
		AnderArtikel bon = new AnderArtikel("omschr", new Geld(12), 42.0);

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitReparatiesBon() {
		ReparatiesBonAdaptee adaptee = new ReparatiesBonAdaptee();
		adaptee.setOmschrijving("omschr");
		adaptee.setBonnummer("bonn");
		adaptee.setLoon(new Geld(12));
		adaptee.setMateriaal(new Geld(1334));
		ReparatiesBon bon = new ReparatiesBon("omschr", "bonn", new Geld(12), new Geld(1334));

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitInstantLoon() {
		InstantLoonAdaptee adaptee = new InstantLoonAdaptee();
		adaptee.setOmschrijving("omschr");
		adaptee.setLoon(new Geld(12));
		adaptee.setLoonBtwPercentage(21);
		InstantLoon bon = new InstantLoon("omschr", new Geld(12), 21);

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitProductLoon() {
		ProductLoonAdaptee adaptee = new ProductLoonAdaptee();
		adaptee.setOmschrijving("omschr");
		adaptee.setUren(15);
		adaptee.setUurloon(new Geld(12));
		adaptee.setLoonBtwPercentage(21);
		ProductLoon bon = new ProductLoon("omschr", 15, new Geld(12), 21);

		assertEquals(bon, this.visitor.visit(adaptee));
	}
}
