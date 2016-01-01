package org.rekeningsysteem.test.io.xml.adaptee.particulier.loon;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeTest;

public class InstantLoonAdapteeTest extends ListItemAdapteeTest {

	@Override
	protected InstantLoonAdaptee makeInstance() {
		return new InstantLoonAdaptee();
	}

	@Override
	protected InstantLoonAdaptee getInstance() {
		return (InstantLoonAdaptee) super.getInstance();
	}

	@Test
	public void testSetGetOmschrijving() {
		this.getInstance().setOmschrijving("foobar");
		assertEquals("foobar", this.getInstance().getOmschrijving());
	}

	@Test
	public void testSetGetLoon() {
		Geld loon = new Geld(2.0);
		this.getInstance().setLoon(loon);
		assertEquals(loon, this.getInstance().getLoon());
	}

	@Test
	public void testSetGetLoonBtwPercentage() {
		this.getInstance().setLoonBtwPercentage(0.6);
		assertEquals(0.6, this.getInstance().getLoonBtwPercentage(), 0.0);
	}
}
