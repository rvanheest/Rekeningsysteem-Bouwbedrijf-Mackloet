package org.rekeningsysteem.test.data.particulier.loon;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

@RunWith(MockitoJUnitRunner.class)
public class ProductLoonTest extends LoonTest {

	private ProductLoon item;
	private final double uren = 10;
	private final CurrencyUnit currency = Monetary.getCurrency("EUR");
	private final MonetaryAmount uurloon = Money.of(4, this.currency);
	private final BtwPercentage loonBtwPercentage = new BtwPercentage(10, false);

	@Override
	protected ProductLoon makeInstance() {
		return new ProductLoon("omschrijving", this.uren, this.uurloon, this.loonBtwPercentage);
	}

	@Override
	protected ProductLoon makeInstance(boolean verlegd) {
		return new ProductLoon("omschrijving", this.uren + 2, this.uurloon, new BtwPercentage(10, verlegd));
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetUren() {
		assertEquals(this.uren, this.item.uren(), 0.0);
	}

	@Test
	public void testGetUurloon() {
		assertEquals(this.uurloon, this.item.uurloon());
	}

	@Test
	public void testGetLoon() {
		assertEquals(Money.of(40, this.currency), this.item.loon());
	}
}
