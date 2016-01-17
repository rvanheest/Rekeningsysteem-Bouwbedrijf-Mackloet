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
import org.rekeningsysteem.io.xml.adapter.ListItemUnmarshaller;

public class ListItemUnmarshallerTest {

	private ListItemUnmarshaller visitor;

	@Before
	public void setUp() {
		this.visitor = new ListItemUnmarshaller();
	}

	@Test
	public void testVisitMutatiesBon() {
		MutatiesBonAdaptee adaptee = MutatiesBonAdaptee.build(a -> a
				.setOmschrijving("omschr")
				.setBonnummer("bonn")
				.setPrijs(new Geld(12)));
		MutatiesBon bon = new MutatiesBon("omschr", "bonn", new Geld(12));

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitGebruiktEsselinkArtikel() {
		EsselinkArtikel artikel = new EsselinkArtikel("artNr", "omschr", 1, "eenh", new Geld(12));
		GebruiktEsselinkArtikelAdaptee adaptee = GebruiktEsselinkArtikelAdaptee.build(a -> a
				.setOmschrijving("omschr")
				.setArtikel(artikel)
				.setAantal(2.3)
				.setMateriaalBtwPercentage(4.2));
		GebruiktEsselinkArtikel bon = new GebruiktEsselinkArtikel(artikel, 2.3, 4.2);

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitAnderArtikel() {
		AnderArtikelAdaptee adaptee = AnderArtikelAdaptee.build(a -> a
				.setOmschrijving("omschr")
				.setPrijs(new Geld(12))
				.setMateriaalBtwPercentage(42));
		AnderArtikel bon = new AnderArtikel("omschr", new Geld(12), 42.0);

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitReparatiesBon() {
		ReparatiesBonAdaptee adaptee = ReparatiesBonAdaptee.build(a -> a
				.setOmschrijving("omschr")
				.setBonnummer("bonn")
				.setLoon(new Geld(12))
				.setMateriaal(new Geld(1334)));
		ReparatiesBon bon = new ReparatiesBon("omschr", "bonn", new Geld(12), new Geld(1334));

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitInstantLoon() {
		InstantLoonAdaptee adaptee = InstantLoonAdaptee.build(a -> a
				.setOmschrijving("omschr")
				.setLoon(new Geld(12))
				.setLoonBtwPercentage(21));
		InstantLoon bon = new InstantLoon("omschr", new Geld(12), 21);

		assertEquals(bon, this.visitor.visit(adaptee));
	}

	@Test
	public void testVisitProductLoon() {
		ProductLoonAdaptee adaptee = ProductLoonAdaptee.build(a -> a
				.setOmschrijving("omschr")
				.setUren(15)
				.setUurloon(new Geld(12))
				.setLoonBtwPercentage(21));
		ProductLoon bon = new ProductLoon("omschr", 15, new Geld(12), 21);

		assertEquals(bon, this.visitor.visit(adaptee));
	}
}
