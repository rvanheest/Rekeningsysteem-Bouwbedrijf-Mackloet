package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

public class ReparatiesInkoopOrderTest {

	private ReparatiesInkoopOrder order;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");
	private MonetaryAmount loon;
	private MonetaryAmount materiaal;

	@Before
	public void setUp() {
		this.loon = Money.of(1, this.currency);
		this.materiaal = Money.of(12, this.currency);
		this.order = new ReparatiesInkoopOrder("omschrijving", "ordernummer", this.loon, this.materiaal);
	}

	@Test
	public void testMateriaalBedrag() {
		assertEquals(this.materiaal, this.order.materiaal());
	}

	@Test
	public void testLoonBedrag() {
		assertEquals(this.loon, this.order.loon());
	}

	@Test
	public void testGetTotaal() {
		MonetaryAmount expected = Money.of(this.materiaal.getNumber().doubleValueExact() + this.loon.getNumber().doubleValueExact(), this.currency);
		assertEquals(expected, this.order.getTotaal());
	}
}
