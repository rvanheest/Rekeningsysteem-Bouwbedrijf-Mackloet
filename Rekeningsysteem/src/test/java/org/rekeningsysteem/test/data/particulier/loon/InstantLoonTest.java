package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

public class InstantLoonTest extends LoonTest {

	private InstantLoon item;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");
	private final MonetaryAmount loon = Money.of(12, this.currency);
	private final BtwPercentage loonBtwPercentage = new BtwPercentage(10, false);

	@Override
	protected InstantLoon makeInstance() {
		return new InstantLoon("omschrijving", this.loon, this.loonBtwPercentage);
	}

	@Override
	protected InstantLoon makeInstance(boolean verlegd) {
		return new InstantLoon("omschrijving", this.loon.multiply(2), new BtwPercentage(10, verlegd));
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetLoon() {
		assertEquals(this.loon, this.item.loon());
	}

	@Test
	public void testGetLoonBtwPercentage() {
		assertEquals(this.loonBtwPercentage, this.item.loonBtwPercentage());
	}
}
