package org.rekeningsysteem.test.io.xml.adapter.mutaties;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Currency;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adapter.mutaties.MutatiesFactuurAdapter;

public class MutatiesFactuurAdapterTest {

	private MutatiesFactuurAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new MutatiesFactuurAdapter();
	}

	@Test
	public void testUnmarshalMarshal() {
		ItemList<MutatiesBon> itemList = new ItemList<>();
		itemList.add(new MutatiesBon("omschr", "nr", new Geld(1)));

		MutatiesFactuur expected = new MutatiesFactuur(new FactuurHeader(new Debiteur("a", "b",
				"c", "d", "e", "f"), LocalDate.now(), "g"), Currency.getInstance("EUR"), itemList);

		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
