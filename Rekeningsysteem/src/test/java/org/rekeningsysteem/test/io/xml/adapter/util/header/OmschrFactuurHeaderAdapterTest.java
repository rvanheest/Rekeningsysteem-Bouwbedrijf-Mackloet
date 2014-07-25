package org.rekeningsysteem.test.io.xml.adapter.util.header;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.util.header.OmschrFactuurHeaderAdapter;

public class OmschrFactuurHeaderAdapterTest {

	private OmschrFactuurHeaderAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new OmschrFactuurHeaderAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		OmschrFactuurHeader expected = new OmschrFactuurHeader(new Debiteur("a", "b", "c", "d",
				"e", "f"), LocalDate.now(), "g", "h");
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
