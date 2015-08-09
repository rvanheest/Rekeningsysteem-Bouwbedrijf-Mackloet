package org.rekeningsysteem.test.io.xml.adaptee.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.io.xml.adaptee.util.BtwPercentageAdaptee;

@Deprecated
public class BtwPercentageAdapteeTest {

	private BtwPercentageAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new BtwPercentageAdaptee();
	}

	@Test
	@Ignore
	public void testSetGetLoonPercentage() {
		this.adaptee.setLoonPercentage(12.0);
		assertEquals(12.0, this.adaptee.getLoonPercentage(), 0.0);
	}

	@Test
	@Ignore
	public void testSetGetMateriaalPercentage() {
		this.adaptee.setMateriaalPercentage(4.3);
		assertEquals(4.3, this.adaptee.getMateriaalPercentage(), 0.0);
	}
}
