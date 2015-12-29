package org.rekeningsysteem.test.data.aangenomen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.util.BtwListItemTest;

@Ignore
@Deprecated
public class AangenomenListItemTest extends BtwListItemTest {

	private AangenomenListItem item;
	private final String omschrijving = "omschrijving";
	private final Geld loon = new Geld(20);
	private final double loonBtwPercentage = 10;
	private final Geld materiaal = new Geld(30);
	private final double materiaalBtwPercentage = 50;

	@Override
	protected AangenomenListItem makeInstance() {
		return new AangenomenListItem(this.omschrijving, this.loon, this.loonBtwPercentage,
				this.materiaal, this.materiaalBtwPercentage);
	}

	@Override
	protected AangenomenListItem makeNotInstance() {
		return new AangenomenListItem(this.omschrijving + ".", this.loon, this.loonBtwPercentage,
				this.materiaal, this.materiaalBtwPercentage);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.item = this.makeInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.item.getOmschrijving());
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
	public void testGetMateriaal() {
		assertEquals(this.materiaal, this.item.getMateriaal());
	}

	@Test
	public void testGetMateriaalBtwPercentage() {
		assertEquals(this.materiaalBtwPercentage, this.item.getMateriaalBtwPercentage(), 0.0);
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving + ".", this.loon,
				this.loonBtwPercentage, this.materiaal, this.materiaalBtwPercentage);
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving, this.loon.multiply(2),
				this.loonBtwPercentage, this.materiaal, this.materiaalBtwPercentage);
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testEqualsFalseOtherLoonBtwPercentage() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving, this.loon,
				this.loonBtwPercentage + 1.0, this.materiaal, this.materiaalBtwPercentage);
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testEqualsFalseOtherMateriaal() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving, this.loon,
				this.loonBtwPercentage, this.materiaal.multiply(2), this.materiaalBtwPercentage);
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testEqualsFalseOtherMateriaalBtwPercentage() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving, this.loon,
				this.loonBtwPercentage, this.materiaal, this.materiaalBtwPercentage + 1.0);
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testToString() {
		assertEquals("<AangenomenListItem[omschrijving, <Geld[20,00]>, 10.0, <Geld[30,00]>, "
				+ "50.0]>", this.item.toString());
	}
}
