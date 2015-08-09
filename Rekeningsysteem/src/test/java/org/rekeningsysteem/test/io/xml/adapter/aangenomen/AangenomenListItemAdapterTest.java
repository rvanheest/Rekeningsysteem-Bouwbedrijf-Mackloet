package org.rekeningsysteem.test.io.xml.adapter.aangenomen;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.aangenomen.AangenomenListItemAdapter;

public class AangenomenListItemAdapterTest {

	private AangenomenListItemAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new AangenomenListItemAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		AangenomenListItem expected = new AangenomenListItem("omschr", new Geld(12.3), 6,
				new Geld(49), 21);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
