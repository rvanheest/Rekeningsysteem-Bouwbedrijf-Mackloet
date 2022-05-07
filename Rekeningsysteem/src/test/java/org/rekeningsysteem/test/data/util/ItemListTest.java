package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.Totalen;

import java.util.Arrays;
import java.util.Currency;

public class ItemListTest {

	private final Currency currency = Currency.getInstance("EUR");

	@Test
	public void testGetTotalenEmptyList() {
		assertEquals(Totalen.Empty(), new ItemList<>(this.currency).getTotalen());
	}

	@Test
	public void testGetTotalenNonEmptyList() {
		MutatiesInkoopOrder mutaties = new MutatiesInkoopOrder("", "", new Geld(1));
		AnderArtikel ander1 = new AnderArtikel("", new Geld(2), new BtwPercentage(0.0, false));
		InstantLoon loon = new InstantLoon("", new Geld(4), new BtwPercentage(50.0, false));
		AnderArtikel ander2 = new AnderArtikel("", new Geld(5), new BtwPercentage(25.0, true));

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

		assertEquals(new Geld(12), result.nettoBtwPerPercentage().get(new BtwPercentage(0.0, false)).netto());
		assertEquals(new Geld(0), result.nettoBtwPerPercentage().get(new BtwPercentage(0.0, false)).btw());
		assertEquals(new Geld(20), result.nettoBtwPerPercentage().get(new BtwPercentage(25.0, true)).netto());
		assertEquals(new Geld(5), result.nettoBtwPerPercentage().get(new BtwPercentage(25.0, true)).btw());
		assertEquals(new Geld(16), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).netto());
		assertEquals(new Geld(8), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).btw());
		assertEquals(new Geld(48), result.getSubtotaal()); // 12 + 20 + 16
		assertEquals(new Geld(56), result.getTotaal()); // 48 + (16 * 0.5)
	}

	@Test
	public void testGetTotalenNonEmptyListWithZeros() {
		InstantLoon loon = new InstantLoon("", new Geld(0), new BtwPercentage(21.0, false));
		AnderArtikel materiaal = new AnderArtikel("", new Geld(2), new BtwPercentage(50.0, false));

		ItemList<ListItem> list = new ItemList<>(this.currency, Arrays.asList(loon, materiaal));
		Totalen result = list.getTotalen();

		assertEquals(new Geld(2), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).netto());
		assertEquals(new Geld(1), result.nettoBtwPerPercentage().get(new BtwPercentage(50.0, false)).btw());
		assertEquals(new Geld(0), result.nettoBtwPerPercentage().get(new BtwPercentage(21.0, false)).netto());
		assertEquals(new Geld(0), result.nettoBtwPerPercentage().get(new BtwPercentage(21.0, false)).btw());
		assertEquals(new Geld(2), result.getSubtotaal());
		assertEquals(new Geld(3), result.getTotaal());
	}
}
