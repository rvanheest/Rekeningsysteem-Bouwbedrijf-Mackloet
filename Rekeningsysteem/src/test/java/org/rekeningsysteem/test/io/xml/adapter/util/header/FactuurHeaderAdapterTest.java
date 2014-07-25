package org.rekeningsysteem.test.io.xml.adapter.util.header;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adapter.util.header.FactuurHeaderAdapter;

public class FactuurHeaderAdapterTest {

	private FactuurHeaderAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new FactuurHeaderAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		FactuurHeader expected = new FactuurHeader(new Debiteur("a", "b", "c", "d", "e", "f"),
				LocalDate.now(), "g");
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
