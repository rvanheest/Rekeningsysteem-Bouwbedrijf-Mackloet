package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.loon.Loon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.test.data.util.BtwListItemTest;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public abstract class LoonTest extends BtwListItemTest {

	private Loon loon;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");

	@Override
	protected abstract Loon makeInstance();

	protected abstract Loon makeInstance(boolean verlegd);

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.loon = this.makeInstance();
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(Money.zero(this.currency), this.loon.materiaal());
	}

	@Test
	public void testGetMateriaalBtwPercentageNietVerlegd() {
		assertEquals(new BtwPercentage(0.0, false), this.makeInstance(false).materiaalBtwPercentage());
	}

	@Test
	public void testGetMateriaalBtwPercentageWelVerlegd() {
		assertEquals(new BtwPercentage(0.0, true), this.makeInstance(true).materiaalBtwPercentage());
	}

	@Test
	@Override
	public void testGetMateriaalBtw() {
		assertEquals(Money.zero(this.currency), this.loon.materiaalBtw());
	}
}
