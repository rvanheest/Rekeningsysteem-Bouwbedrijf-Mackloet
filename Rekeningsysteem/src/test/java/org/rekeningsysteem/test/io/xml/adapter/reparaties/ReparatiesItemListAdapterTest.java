package org.rekeningsysteem.test.io.xml.adapter.reparaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adapter.reparaties.ReparatiesItemListAdapter;

public class ReparatiesItemListAdapterTest {

	private ReparatiesItemListAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ReparatiesItemListAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		ItemList<ReparatiesBon> expected = new ItemList<>();
		expected.add(new ReparatiesBon("omschr", "bonnr", new Geld(1.0), new Geld(1.0)));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
