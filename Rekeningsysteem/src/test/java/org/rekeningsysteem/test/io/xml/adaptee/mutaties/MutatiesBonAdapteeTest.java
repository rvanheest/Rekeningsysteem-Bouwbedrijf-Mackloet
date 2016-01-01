package org.rekeningsysteem.test.io.xml.adaptee.mutaties;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;
import org.rekeningsysteem.test.io.xml.adaptee.ListItemAdapteeTest;

public class MutatiesBonAdapteeTest extends ListItemAdapteeTest {

	@Override
	protected MutatiesBonAdaptee makeInstance() {
		return new MutatiesBonAdaptee();
	}

	@Override
	protected MutatiesBonAdaptee getInstance() {
		return (MutatiesBonAdaptee) super.getInstance();
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
	public void testSetGetPrijs() {
		Geld prijs = new Geld(12.04);
		this.getInstance().setPrijs(prijs);
		assertEquals(prijs, this.getInstance().getPrijs());
	}
}
