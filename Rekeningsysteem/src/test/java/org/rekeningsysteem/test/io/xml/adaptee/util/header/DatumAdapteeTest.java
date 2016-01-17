package org.rekeningsysteem.test.io.xml.adaptee.util.header;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adaptee.util.header.DatumAdaptee;

public class DatumAdapteeTest {

	private final int dag = 30;
	private final int maand = 7;
	private final int jaar = 1992;
	private DatumAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = DatumAdaptee.build(adaptee -> adaptee
				.setDag(this.dag)
				.setMaand(this.maand)
				.setJaar(this.jaar));
	}

	@Test
	public void testSetGetDag() {
		assertEquals(this.dag, this.adaptee.getDag());
	}

	@Test
	public void testSetGetMaand() {
		assertEquals(this.maand, this.adaptee.getMaand());
	}

	@Test
	public void testSetGetJaar() {
		assertEquals(this.jaar, this.adaptee.getJaar());
	}
}
