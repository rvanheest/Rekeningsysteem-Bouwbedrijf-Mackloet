package org.rekeningsysteem.test.io.xml.adapter.util.header;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.xml.adapter.util.header.DebiteurAdapter;

public class DebiteurAdapterTest {

	private DebiteurAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new DebiteurAdapter();
	}

	@Test
	public void testMarhalUnmarshal() {
		Debiteur expected = new Debiteur("naam", "straat", "nummer", "postcode", "plaats",
				"btwNummer");
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
