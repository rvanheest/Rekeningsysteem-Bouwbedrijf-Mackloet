package org.rekeningsysteem.test.data.mutaties;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

public class MutatiesInkoopOrderTest {

	private MutatiesInkoopOrder order;
	private MonetaryAmount materiaal;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");

	@Before
	public void setUp() {
		this.materiaal = Money.of(10, this.currency);
		this.order = new MutatiesInkoopOrder("omschrijving", "ordernummer", this.materiaal);
	}

	@Test
	public void testMateriaalBedrag() {
		assertEquals(this.materiaal, this.order.materiaal());
	}

	@Test
	public void testGeenLoonBedrag() {
		assertEquals(Money.zero(this.currency), this.order.loon());
	}

	@Test
	public void testGetTotaal() {
		assertEquals(this.materiaal, this.order.getTotaal());
	}
}
