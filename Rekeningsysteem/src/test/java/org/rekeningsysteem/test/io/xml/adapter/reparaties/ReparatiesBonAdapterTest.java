package org.rekeningsysteem.test.io.xml.adapter.reparaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.reparaties.ReparatiesBonAdapter;

public class ReparatiesBonAdapterTest {

	private ReparatiesBonAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new ReparatiesBonAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		ReparatiesBon expected = new ReparatiesBon("omschr", "bonnummer", new Geld(1), new Geld(3));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
