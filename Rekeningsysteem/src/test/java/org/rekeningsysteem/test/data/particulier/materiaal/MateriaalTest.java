package org.rekeningsysteem.test.data.particulier.materiaal;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.materiaal.Materiaal;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.test.data.util.BtwListItemTest;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

import static org.junit.Assert.assertEquals;

public abstract class MateriaalTest extends BtwListItemTest {

	private Materiaal materiaal;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");

	@Override
	protected abstract Materiaal makeInstance();

	protected abstract Materiaal makeInstance(boolean verlegd);

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.materiaal = this.makeInstance();
	}

	@Test
	public void testGetLoon() {
		assertEquals(Money.zero(this.currency), this.materiaal.loon());
	}

	@Test
	public void testGetLoonBtwPercentageNietVerlegd() {
		assertEquals(new BtwPercentage(0.0, false), this.makeInstance(false).loonBtwPercentage());
	}

	@Test
	public void testGetLoonBtwPercentageWelVerlegd() {
		assertEquals(new BtwPercentage(0.0, true), this.makeInstance(true).loonBtwPercentage());
	}

	@Test
	@Override
	public void testGetLoonBtw() {
		assertEquals(Money.zero(this.currency), this.materiaal.loonBtw());
	}
}
