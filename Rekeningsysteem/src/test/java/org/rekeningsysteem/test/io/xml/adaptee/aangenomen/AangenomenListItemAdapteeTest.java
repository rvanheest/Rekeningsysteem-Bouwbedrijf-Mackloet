package org.rekeningsysteem.test.io.xml.adaptee.aangenomen;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.aangenomen.AangenomenListItemAdaptee;

@Ignore
@Deprecated
public class AangenomenListItemAdapteeTest {

	private AangenomenListItemAdaptee adaptee;

	@Before
	public void setUp() {
		this.adaptee = new AangenomenListItemAdaptee();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.adaptee.setOmschrijving("omschr");
		assertEquals("omschr", this.adaptee.getOmschrijving());
	}

	@Test
	public void testSetGetLoon() {
		Geld loon = new Geld(90);
		this.adaptee.setLoon(loon);
		assertEquals(loon, this.adaptee.getLoon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		this.adaptee.setLoonBtwPercentage(0.2);
		assertEquals(0.2, this.adaptee.getLoonBtwPercentage(), 0.0);
	}

	@Test
	public void testSetGetMateriaal() {
		Geld materiaal = new Geld(90);
		this.adaptee.setMateriaal(materiaal);
		assertEquals(materiaal, this.adaptee.getMateriaal());
	}

	@Test
	public void testSetGetMateriaalBtwPercentage() {
		this.adaptee.setMateriaalBtwPercentage(0.3);
		assertEquals(0.3, this.adaptee.getMateriaalBtwPercentage(), 0.0);
	}
}
