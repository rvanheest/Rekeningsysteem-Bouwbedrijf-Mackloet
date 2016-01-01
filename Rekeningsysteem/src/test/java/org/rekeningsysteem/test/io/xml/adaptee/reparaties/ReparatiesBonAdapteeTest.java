package org.rekeningsysteem.test.io.xml.adaptee.reparaties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeTest;

public class ReparatiesBonAdapteeTest extends ListItemAdapteeTest {

	@Override
	protected ReparatiesBonAdaptee makeInstance() {
		return new ReparatiesBonAdaptee();
	}

	@Override
	protected ReparatiesBonAdaptee getInstance() {
		return (ReparatiesBonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("omschr");
		assertEquals("omschr", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetBonnummer() {
		this.getInstance().setBonnummer("bonnr");
		assertEquals("bonnr", this.getInstance().getBonnummer());
	}

	@Test
	public void testSetGetLoon() {
		Geld loon = new Geld(12.04);
		this.getInstance().setLoon(loon);
		assertEquals(loon, this.getInstance().getLoon());
	}

	@Test
	public void testSetGetMateriaal() {
		Geld materiaal = new Geld(16.40);
		this.getInstance().setMateriaal(materiaal);
		assertEquals(materiaal, this.getInstance().getMateriaal());
	}
}
