package org.rekeningsysteem.test.data.util.loon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.loon.InstantLoon;

public class InstantLoonTest extends AbstractLoonTest {

	private final Geld loon = new Geld(12);

	@Override
	protected InstantLoon makeInstance() {
		return new InstantLoon(this.getTestOmschrijving(), this.loon);
	}

	@Override
	protected Object makeNotInstance() {
		return new InstantLoon(this.getTestOmschrijving(), this.loon.add(new Geld(1)));
	}

	@Override
	protected InstantLoon getInstance() {
		return (InstantLoon) super.getInstance();
	}

	@Test
	public void testGetLoon() {
		assertEquals(this.loon, this.getInstance().getLoon());
	}
	
	@Override
	public void testGetTotaal() {
		assertEquals(this.loon, this.getInstance().getTotaal());
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		InstantLoon loon2 = new InstantLoon(this.getTestOmschrijving(),
				this.loon.add(new Geld(10)));
		assertFalse(this.getInstance().equals(loon2));
	}

	@Test
	public void testToString() {
		assertEquals("<InstantLoon[omschrijving, <Geld[12,00]>]>", this.getInstance().toString());
	}
}
