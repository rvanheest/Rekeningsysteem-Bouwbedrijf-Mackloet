package org.rekeningsysteem.test.data.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.TotalenListItemVisitor;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;

@RunWith(MockitoJUnitRunner.class)
public class ItemListTest extends EqualsHashCodeTest {

	private ItemList<ListItem> list;
	private final TotalenListItemVisitor visitor = new TotalenListItemVisitor();

	private final MutatiesInkoopOrder mutaties = new MutatiesInkoopOrder("", "", new Geld(1));
	private final AnderArtikel ander1 = new AnderArtikel("", new Geld(2), new BtwPercentage(0.0, false));
	private final InstantLoon loon = new InstantLoon("", new Geld(4), new BtwPercentage(50.0, false));
	private final AnderArtikel ander2 = new AnderArtikel("", new Geld(5), new BtwPercentage(25.0, true));

	@Override
	protected ItemList<ListItem> makeInstance() {
		ItemList<ListItem> l = new ItemList<>(this.visitor);
		l.add(this.mutaties);
		l.add(this.ander1);
		l.add(this.loon);
		l.add(this.ander2);
		return l;
	}

	@Override
	protected ItemList<ListItem> makeNotInstance() {
		ItemList<ListItem> l = new ItemList<>(this.visitor);
		l.add(this.mutaties);
		l.add(this.ander1);
		return l;
	}

	@Before
	public void setUp() {
		super.setUp();
		this.list = this.makeInstance();
	}

	@Test
	public void testGetTotalenEmptyList() {
		assertEquals(new Totalen(), new ItemList<>().getTotalen());
	}

	@Test
	public void testGetTotalenNonEmptyList() {
		this.list.add(this.mutaties);
		this.list.add(this.ander1);
		this.list.add(this.loon);
		this.list.add(this.ander2);
		this.list.add(this.mutaties);
		this.list.add(this.ander1);
		this.list.add(this.loon);
		this.list.add(this.ander2);
		this.list.add(this.mutaties);
		this.list.add(this.ander1);
		this.list.add(this.loon);
		this.list.add(this.ander2);

		Totalen result = this.list.getTotalen();

		Map<BtwPercentage, Geld> expectedBtw = new HashMap<>();
		expectedBtw.put(new BtwPercentage(50.0, false), new Geld(8));
		expectedBtw.put(new BtwPercentage(25.0, true), new Geld(0));
		expectedBtw.put(new BtwPercentage(0.0, false), new Geld(0));

		assertEquals(new Geld(12), result.getNetto().get(new BtwPercentage(0.0, false)));
		assertEquals(new Geld(20), result.getNetto().get(new BtwPercentage(25.0, true)));
		assertEquals(new Geld(16), result.getNetto().get(new BtwPercentage(50.0, false)));
		assertEquals(new Geld(8), result.getBtw().get(new BtwPercentage(50.0, false)));
		assertEquals(new Geld(0), result.getBtw().get(new BtwPercentage(25.0, true)));
		assertEquals(expectedBtw, result.getBtw());
		assertEquals(new Geld(48), result.getSubtotaal()); // 12 + 20 + 16
		assertEquals(new Geld(56), result.getTotaal()); // 48 + (16 * 0.5)
	}

	@Test
	public void testGetTotalenNonEmptyListWithZeros() {
		this.list.clear();

		InstantLoon loon = new InstantLoon("", new Geld(0), new BtwPercentage(21.0, false));
		AnderArtikel materiaal = new AnderArtikel("", new Geld(2), new BtwPercentage(50.0, false));

		this.list.add(loon);
		this.list.add(materiaal);

		Totalen result = this.list.getTotalen();

		Map<BtwPercentage, Geld> expectedBtw = new HashMap<>();
		expectedBtw.put(new BtwPercentage(50.0, false), new Geld(1));
		expectedBtw.put(new BtwPercentage(21.0, false), new Geld(0.0));

		assertEquals(new Geld(2), result.getNetto().get(new BtwPercentage(50.0, false)));
		assertEquals(new Geld(1), result.getBtw().get(new BtwPercentage(50.0, false)));
		assertEquals(expectedBtw, result.getBtw());
		assertEquals(new Geld(2), result.getSubtotaal());
		assertEquals(new Geld(3), result.getTotaal());
	}
}
