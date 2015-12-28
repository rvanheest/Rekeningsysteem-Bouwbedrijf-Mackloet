package org.rekeningsysteem.test.data.particulier2.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier2.loon.InstantLoon2;
import org.rekeningsysteem.data.util.Geld;

public class InstantLoon2Test extends AbstractLoon2Test {

	private InstantLoon2 item;
	private final Geld loon = new Geld(12);
	private final double loonBtwPercentage = 10;

	@Override
	protected InstantLoon2 makeInstance() {
		return new InstantLoon2(this.getTestOmschrijving(), this.loon, this.loonBtwPercentage);
	}

	@Override
	protected InstantLoon2 makeNotInstance() {
		return new InstantLoon2(this.getTestOmschrijving(), this.loon.multiply(2),
				this.loonBtwPercentage);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetLoon() {
		assertEquals(this.loon, this.item.getLoon());
	}

	@Test
	public void testGetLoonBtwPercentage() {
		assertEquals(this.loonBtwPercentage, this.item.getLoonBtwPercentage(), 0.0);
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		InstantLoon2 loon2 = new InstantLoon2(this.getTestOmschrijving() + ".", this.loon,
				this.loonBtwPercentage);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		InstantLoon2 loon2 = new InstantLoon2(this.getTestOmschrijving(),
				this.loon.multiply(2), this.loonBtwPercentage);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testEqualsFalseOtherLoonBtwPercentage() {
		InstantLoon2 loon2 = new InstantLoon2(this.getTestOmschrijving(),
				this.loon, this.loonBtwPercentage + 1.0);
		assertFalse(this.item.equals(loon2));
	}

	@Test
	public void testToString() {
		assertEquals("<InstantLoon[omschrijving, <Geld[12,00]>, 10.0]>", this.item.toString());
	}
}
