package org.rekeningsysteem.test.io.xml.adapter.mutaties;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.mutaties.MutatiesBonAdapter;

public class MutatiesBonAdapterTest {

	private MutatiesBonAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new MutatiesBonAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		MutatiesBon expected = new MutatiesBon("omschr", "nr", new Geld(1));
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
