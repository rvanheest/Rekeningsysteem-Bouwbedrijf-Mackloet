package org.rekeningsysteem.test.io.xml.adapter.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;

public class BtwPercentageAdapterTest {

	private BtwPercentageAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new BtwPercentageAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		BtwPercentage expected = new BtwPercentage(6, 21);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
