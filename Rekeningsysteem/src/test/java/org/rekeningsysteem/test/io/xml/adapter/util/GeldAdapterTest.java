package org.rekeningsysteem.test.io.xml.adapter.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

public class GeldAdapterTest {

	private GeldAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new GeldAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		Geld expected = new Geld(21);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
