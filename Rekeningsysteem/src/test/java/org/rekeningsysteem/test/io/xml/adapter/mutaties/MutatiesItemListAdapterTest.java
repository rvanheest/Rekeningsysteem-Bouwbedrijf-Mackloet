package org.rekeningsysteem.test.io.xml.adapter.mutaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adapter.mutaties.MutatiesItemListAdapter;

public class MutatiesItemListAdapterTest {

	private MutatiesItemListAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new MutatiesItemListAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		ItemList<MutatiesBon> expected = new ItemList<>();
		expected.add(new MutatiesBon("omschr", "bonnr", new Geld(1.0)));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
