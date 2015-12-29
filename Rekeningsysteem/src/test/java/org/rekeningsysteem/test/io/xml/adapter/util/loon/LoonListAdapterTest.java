package org.rekeningsysteem.test.io.xml.adapter.util.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.io.xml.adapter.util.loon.LoonListAdapter;

@Deprecated
public class LoonListAdapterTest {

	private LoonListAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new LoonListAdapter();
	}

	@Test
	public void testMarshalUnMarshal() {
		ItemList<AbstractLoon> expected = new ItemList<>();
		expected.add(new InstantLoon("omschr", new Geld(1.0), 6));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
