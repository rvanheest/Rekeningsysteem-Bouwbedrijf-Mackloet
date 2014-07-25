package org.rekeningsysteem.test.io.xml.adapter.util.header;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adapter.util.header.DatumAdapter;

public class DatumAdapterTest {

	private DatumAdapter adapter;

	@Before
	public void setUp() {
		this.adapter = new DatumAdapter();
	}

	@Test
	public void testMarshalUnmarshal() {
		LocalDate expected = LocalDate.of(1992, 7, 30);
		assertEquals(expected, this.adapter.unmarshal(this.adapter.marshal(expected)));
	}
}
