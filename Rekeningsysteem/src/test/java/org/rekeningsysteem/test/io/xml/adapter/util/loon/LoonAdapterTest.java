package org.rekeningsysteem.test.io.xml.adapter.util.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;
import org.rekeningsysteem.io.xml.adapter.util.loon.LoonAdapter;
@Deprecated
public class LoonAdapterTest {

	private LoonAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new LoonAdapter();
	}

	@Test
	public void testInstantLoonMarshalUnmarshal() {
		InstantLoon expected = new InstantLoon("omschr", new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}

	@Test
	public void testProductLoonMarshalUnmarshal() {
		ProductLoon expected = new ProductLoon("omschr", 3.0, new Geld(1.0), 6);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
