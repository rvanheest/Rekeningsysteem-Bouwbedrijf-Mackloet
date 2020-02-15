package org.rekeningsysteem.test.io.pdf;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import org.rekeningsysteem.io.pdf.PdfListItemVisitor;

public class PdfListItemVisitorTest {

	private PdfListItemVisitor visitor;

	@Before
	public void setUp() {
		this.visitor = new PdfListItemVisitor();
	}

	@Test
	public void testVisitMutatiesInkoopOrder() {
		MutatiesInkoopOrder item = new MutatiesInkoopOrder("omschr", "ordernr", new Geld(12.34));
		List<String> expected = Arrays.asList("omschr", "ordernr", "12,34");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitMutatiesInkoopOrderZero() {
		MutatiesInkoopOrder item = new MutatiesInkoopOrder("omschr", "ordernr", new Geld(0.0));
		List<String> expected = Collections.emptyList();

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testGebruiktEsselinkArtikel() {
		EsselinkArtikel artikel = new EsselinkArtikel("art", "omschr", 5, "eenh", new Geld(2.0));
		GebruiktEsselinkArtikel item = new GebruiktEsselinkArtikel(artikel, 2.0, new BtwPercentage(19.0, false));
		List<String> expected = Arrays.asList("2x omschr", "0,80", "19.0%");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testGebruiktEsselinkArtikelZero() {
		EsselinkArtikel artikel = new EsselinkArtikel("art", "omschr", 5, "eenh", new Geld(0.0));
		GebruiktEsselinkArtikel item = new GebruiktEsselinkArtikel(artikel, 2.0, new BtwPercentage(19.0, false));
		List<String> expected = Collections.emptyList();

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testGebruiktEsselinkArtikelInfinite() {
		EsselinkArtikel artikel = new EsselinkArtikel("art", "omschr", 5, "eenh", new Geld(2.0));
		GebruiktEsselinkArtikel item = new GebruiktEsselinkArtikel(artikel, Double.POSITIVE_INFINITY, new BtwPercentage(19.0, false));
		List<String> expected = Arrays.asList("Infinityx omschr", "18.446.744.073.709.552,00", "19.0%");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testGebruiktEsselinkArtikelDoubleAantal() {
		EsselinkArtikel artikel = new EsselinkArtikel("art", "omschr", 5, "eenh", new Geld(2.0));
		GebruiktEsselinkArtikel item = new GebruiktEsselinkArtikel(artikel, 2.3, new BtwPercentage(19.0, false));
		List<String> expected = Arrays.asList("2.3x omschr", "0,92", "19.0%");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitAnderArtikel() {
		AnderArtikel item = new AnderArtikel("omschr", new Geld(12.34), new BtwPercentage(19.0, false));
		List<String> expected = Arrays.asList("omschr", "12,34", "19.0%");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitAnderArtikelZero() {
		AnderArtikel item = new AnderArtikel("omschr", new Geld(0), new BtwPercentage(19.0, false));
		List<String> expected = Collections.emptyList();

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitReparatiesInkoopOrder() {
		ReparatiesInkoopOrder item = new ReparatiesInkoopOrder("omschr", "ordernr", new Geld(12.34), new Geld(56.78));
		List<String> expected = Arrays.asList("omschr", "ordernr", "12,34", "56,78", "69,12");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitReparatiesInkoopOrderZero() {
		ReparatiesInkoopOrder item = new ReparatiesInkoopOrder("omschr", "ordernr", new Geld(0.0), new Geld(0.0));
		List<String> expected = Collections.emptyList();

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitInstantLoon() {
		InstantLoon item = new InstantLoon("omschr", new Geld(12.34), new BtwPercentage(19.0, false));
		List<String> expected = Arrays.asList("omschr", "12,34", "19.0%");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitInstantLoonZero() {
		InstantLoon item = new InstantLoon("omschr", new Geld(0), new BtwPercentage(19.0, false));
		List<String> expected = Collections.emptyList();

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitProductLoon() {
		ProductLoon item = new ProductLoon("omschr", 2.0, new Geld(12.34), new BtwPercentage(19.0, false));
		List<String> expected = Arrays.asList("2.0 uren à 12,34", "24,68", "19.0%");

		assertEquals(expected, this.visitor.visit(item));
	}

	@Test
	public void testVisitProductLoonZero() {
		ProductLoon item = new ProductLoon("omschr", 2.0, new Geld(0), new BtwPercentage(19.0, false));
		List<String> expected = Collections.emptyList();

		assertEquals(expected, this.visitor.visit(item));
	}
}
