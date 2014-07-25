package org.rekeningsysteem.test.io.xml.adaptee.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adaptee.util.GeldAdaptee;

public class GeldAdapteeTest {

	private GeldAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new GeldAdaptee();
	}

	@Test
	public void testSetGetBedrag() {
		this.adaptee.setBedrag(12.54);
		assertEquals(12.54, this.adaptee.getBedrag(), 0.0);
	}
}
