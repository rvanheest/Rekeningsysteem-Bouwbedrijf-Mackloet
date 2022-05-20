package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.javamoney.moneta.Money;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.Totalen;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.Arrays;

public class ItemListTest {

	private final CurrencyUnit currency = Monetary.getCurrency("EUR");

	@Test
	public void testGetTotalenEmptyList() {
		assertEquals(new Totalen(this.currency), new ItemList<>(this.currency).getTotalen());
	}

	@Test
	public void testGetTotalenNonEmptyList() {
		MutatiesInkoopOrder mutaties = new MutatiesInkoopOrder("", "", Money.of(1, this.currency));
		AnderArtikel ander1 = new AnderArtikel("", Money.of(2, this.currency), new BtwPercentage(0.0, false));
		InstantLoon loon = new InstantLoon("", Money.of(4, this.currency), new BtwPercentage(50.0, false));
		AnderArtikel ander2 = new AnderArtikel("", Money.of(5, this.currency), new BtwPercentage(25.0, true));

		ItemList<ListItem> list = new ItemList<>(this.currency, Arrays.asList(
				mutaties,
				ander1,
				loon,
				ander2,
				mutaties,
				ander1,
				loon,
				ander2,
				mutaties,
				ander1,
				loon,
				ander2,
				mutaties,
				ander1,
				loon,
				ander2
		));

		Totalen result = list.getTotalen();

		assertEquals(Money.of(12, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(0.0, false)).netto());
		assertEquals(Money.of(0, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(0.0, false)).btw());
		assertEquals(Money.of(20, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(25.0, true)).netto());
		assertEquals(Money.of(5, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(25.0, true)).btw());
		assertEquals(Money.of(16, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).netto());
		assertEquals(Money.of(8, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).btw());
		assertEquals(Money.of(48, this.currency), result.getSubtotaal()); // 12 + 20 + 16
		assertEquals(Money.of(56, this.currency), result.getTotaal()); // 48 + (16 * 0.5)
	}

	@Test
	public void testGetTotalenNonEmptyListWithZeros() {
		InstantLoon loon = new InstantLoon("", Money.of(0, this.currency), new BtwPercentage(21.0, false));
		AnderArtikel materiaal = new AnderArtikel("", Money.of(2, this.currency), new BtwPercentage(50.0, false));

		ItemList<ListItem> list = new ItemList<>(this.currency, Arrays.asList(loon, materiaal));
		Totalen result = list.getTotalen();

		assertEquals(Money.of(2, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).netto());
		assertEquals(Money.of(1, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).btw());
		assertEquals(Money.of(0, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(21.0, false)).netto());
		assertEquals(Money.of(0, this.currency), result.nettoBtwPerPercentage().get(new BtwPercentage(21.0, false)).btw());
		assertEquals(Money.of(2, this.currency), result.getSubtotaal());
		assertEquals(Money.of(3, this.currency), result.getTotaal());
	}
}
