package org.rekeningsysteem.test.io.xml.adapter.aangenomen;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adapter.aangenomen.AangenomenItemListAdapter;

public class AangenomenItemListAdapterTest {

	private AangenomenItemListAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new AangenomenItemListAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		ItemList<AangenomenListItem> expected = new ItemList<>();
		expected.add(new AangenomenListItem("omschr", new Geld(1.0), 6, new Geld(2.0), 21));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
