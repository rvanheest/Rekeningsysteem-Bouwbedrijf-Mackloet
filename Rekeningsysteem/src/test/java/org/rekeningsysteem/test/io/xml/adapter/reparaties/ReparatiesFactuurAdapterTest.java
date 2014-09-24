package org.rekeningsysteem.test.io.xml.adapter.reparaties;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adapter.reparaties.ReparatiesFactuurAdapter;

public class ReparatiesFactuurAdapterTest {

	private ReparatiesFactuurAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ReparatiesFactuurAdapter();
	}

	@Test
	public void testUnmarshalMarshal() {
		ItemList<ReparatiesBon> itemList = new ItemList<>();
		itemList.add(new ReparatiesBon("omschr", "bonnummer", new Geld(1), new Geld(3)));

		ReparatiesFactuur expected = new ReparatiesFactuur(new FactuurHeader(new Debiteur("a", "b",
				"c", "d", "e", "f"), LocalDate.now(), "g"), Currency.getInstance("EUR"), itemList,
				new BtwPercentage(6, 21));

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
