package org.rekeningsysteem.test.io.xml.adaptee.util.header;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adaptee.util.header.DatumAdaptee;

public class DatumAdapteeTest {

	private DatumAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new DatumAdaptee();
	}

	@Test
	public void testSetGetDag() {
		this.adaptee.setDag(30);
		assertEquals(30, this.adaptee.getDag());
	}

	@Test
	public void testSetGetMaand() {
		this.adaptee.setMaand(7);
		assertEquals(7, this.adaptee.getMaand());
	}

	@Test
	public void testSetGetJaar() {
		this.adaptee.setJaar(1992);
		assertEquals(1992, this.adaptee.getJaar());
	}
}
