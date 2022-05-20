package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwListItem;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

public abstract class BtwListItemTest {

	private BtwListItem item;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");

	protected abstract BtwListItem makeInstance();

	@Before
	public void setUp() {
		this.item = this.makeInstance();
	}

	@Test
	public void testGetLoonBtw() {
		MonetaryAmount loon = this.item.loon();
		double percentage = this.item.loonBtwPercentage().percentage();

		MonetaryAmount expected = Money.of(loon.getNumber().doubleValueExact() * percentage / 100, this.currency);
		assertEquals(expected, this.item.loonBtw());
	}

	@Test
	public void testGetMateriaalBtw() {
		MonetaryAmount materiaal = this.item.materiaal();
		double percentage = this.item.materiaalBtwPercentage().percentage();

		MonetaryAmount expected = Money.of(materiaal.getNumber().doubleValueExact() * percentage / 100, this.currency);
		assertEquals(expected, this.item.materiaalBtw());
	}
}
