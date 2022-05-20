package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Totalen;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class TotalenTest {

	private Totalen totalen;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");

	@Before
	public void setUp() {
		this.totalen = new Totalen(this.currency, new BtwPercentage(50.0, false), Money.of(2, this.currency), Money.of(16, this.currency));
	}

	@Test
	public void testAdd() {
		this.totalen = this.totalen.add(new BtwPercentage(50, false), Money.of(0, this.currency), Money.of(20, this.currency))
				.add(new BtwPercentage(50, false), Money.of(0, this.currency), Money.of(30, this.currency))
				.add(new BtwPercentage(10, true), Money.of(0, this.currency), Money.of(100, this.currency))
				.add(new BtwPercentage(20, true), Money.of(0, this.currency), Money.of(100, this.currency));

		assertEquals(Money.of(2, this.currency), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).netto());
		assertEquals(Money.of(66, this.currency), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).btw());
		assertEquals(Money.of(0, this.currency), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(10.0, true)).netto());
		assertEquals(Money.of(100, this.currency), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(10.0, true)).btw());
		assertEquals(Money.of(0, this.currency), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(20.0, true)).netto());
		assertEquals(Money.of(100, this.currency), this.totalen.nettoBtwPerPercentage().get(new BtwPercentage(20.0, true)).btw());
		assertEquals(Money.of(2, this.currency), this.totalen.getSubtotaal());
		assertEquals(Money.of(68, this.currency), this.totalen.getTotaal()); // 2 + 16 + 20 + 30
	}

	@Test
	public void testGetSubtotalen() {
		assertEquals(Money.of(2, this.currency), this.totalen.getSubtotaal());
	}

	@Test
	public void testGetTotalen() {
		assertEquals(Money.of(18, this.currency), this.totalen.getTotaal());
	}

	@Test
	public void testPlus() {
		Totalen t2 = new Totalen(this.currency, new BtwPercentage(20, true), Money.of(3, this.currency), Money.of(100, this.currency));

		Totalen expected = new Totalen(this.currency)
				.add(new BtwPercentage(50, false), Money.of(2, this.currency), Money.of(16, this.currency))
				.add(new BtwPercentage(20, true), Money.of(3, this.currency), Money.of(100, this.currency));

		assertEquals(expected, this.totalen.plus(t2));
	}
}
