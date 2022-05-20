package org.rekeningsysteem.test.data.particulier.materiaal;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.materiaal.EsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.materiaal.Materiaal;
import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class GebruiktEsselinkArtikelTest extends MateriaalTest {

	private GebruiktEsselinkArtikel gebruiktArtikel;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");
	private final EsselinkArtikel artikel = new EsselinkArtikel("artikelnummer", "artikelomschrijving", 2, "eenheid", Money.of(1, this.currency));

	@Override
	protected GebruiktEsselinkArtikel makeInstance() {
		return new GebruiktEsselinkArtikel("omschrijving", this.artikel, 5, new BtwPercentage(10, false));
	}

	@Override
	protected Materiaal makeInstance(boolean verlegd) {
		return new GebruiktEsselinkArtikel("omschrijving", this.artikel, 5, new BtwPercentage(10, verlegd));
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.gebruiktArtikel = this.makeInstance();
	}

	@Test
	public void testSecondConstructor() {
		this.gebruiktArtikel = new GebruiktEsselinkArtikel(this.artikel, 5, new BtwPercentage(10, false));

		assertEquals(this.artikel.omschrijving(), this.gebruiktArtikel.omschrijving());
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(Money.of(2.5, this.currency), this.gebruiktArtikel.materiaal());
	}
}
