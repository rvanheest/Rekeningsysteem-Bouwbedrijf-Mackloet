package org.rekeningsysteem.test.data.aangenomen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.EqualsHashCodeTest;
import org.rekeningsysteem.test.data.util.ListItemTest;

public class AangenomenListItemTest extends EqualsHashCodeTest implements ListItemTest {

	private AangenomenListItem item;
	private final String omschrijving = "omschrijving";
	private final Geld loon = new Geld(20);
	private final Geld materiaal = new Geld(30);

	@Override
	protected Object makeInstance() {
		return new AangenomenListItem(this.omschrijving, this.loon, this.materiaal);
	}

	@Override
	protected Object makeNotInstance() {
		return new AangenomenListItem(this.omschrijving + ".", this.loon, this.materiaal);
	}

	@Override
	@Before
	public void setUp() {
		super.setUp();
		this.item = new AangenomenListItem(this.omschrijving, this.loon, this.materiaal);
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.item.getOmschrijving());
	}

	@Test
	@Override
	public void testGetLoon() {
		assertEquals(this.loon, this.item.getLoon());
	}

	@Test
	@Override
	public void testGetMateriaal() {
		assertEquals(this.materiaal, this.item.getMateriaal());
	}

	@Test
	@Override
	public void testGetTotaal() {
		assertEquals(new Geld(50), this.item.getTotaal());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving + ".", this.loon,
				this.materiaal);
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving, new Geld(10),
				this.materiaal);
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testEqualsFalseOtherMateriaal() {
		AangenomenListItem ali2 = new AangenomenListItem(this.omschrijving, this.loon,
				new Geld(10));
		assertFalse(this.item.equals(ali2));
	}

	@Test
	public void testToString() {
		assertEquals("<AangenomenListItem[omschrijving, <Geld[20,00]>, <Geld[30,00]>]>",
				this.item.toString());
	}
}
