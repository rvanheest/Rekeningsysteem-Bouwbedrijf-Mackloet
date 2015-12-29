package org.rekeningsysteem.test.io.xml.adaptee.util.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.util.loon.ProductLoonAdaptee;

@Deprecated
public class ProductLoonAdapteeTest extends AbstractLoonAdapteeTest {

	private ProductLoonAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new ProductLoonAdaptee();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.adaptee.setOmschrijving("foobar");
		assertEquals("foobar", this.adaptee.getOmschrijving());
	}

	@Test
	public void testSetGetUren() {
		this.adaptee.setUren(1.0);
		assertEquals(1.0, this.adaptee.getUren(), 0.0);
	}

	@Test
	public void testSetGetUurloon() {
		Geld uurloon = new Geld(1.0);
		this.adaptee.setUurloon(uurloon);
		assertEquals(uurloon, this.adaptee.getUurloon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		this.adaptee.setLoonBtwPercentage(0.4);
		assertEquals(0.4, this.adaptee.getLoonBtwPercentage(), 0.0);
	}
}
