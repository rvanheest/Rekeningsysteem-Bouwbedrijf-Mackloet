package org.rekeningsysteem.test.data.reparaties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.test.data.util.ListItemTest;

public class ReparatiesBonTest extends ListItemTest {

	private ReparatiesBon bon;
	private final String omschrijving = "omschrijving";
	private final String bonnummer = "bonnummer";
	private final Geld loon = new Geld(1);
	private final Geld materiaal = new Geld(12);

	@Override
	protected ReparatiesBon makeInstance() {
		return new ReparatiesBon(this.omschrijving, this.bonnummer, this.loon, this.materiaal);
	}

	@Override
	protected ReparatiesBon makeNotInstance() {
		return new ReparatiesBon(this.omschrijving + ".", this.bonnummer, this.loon,
				this.materiaal);
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.bon = this.makeInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.bon.getOmschrijving());
	}

	@Test
	public void testGetBonnummer() {
		assertEquals(this.bonnummer, this.bon.getBonnummer());
	}

	@Test
	public void testGetLoon() {
		assertEquals(this.loon, this.bon.getLoon());
	}

	@Test
	public void testGetMateriaal() {
		assertEquals(this.materiaal, this.bon.getMateriaal());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		ReparatiesBon mb = new ReparatiesBon(this.omschrijving + ".", this.bonnummer, this.loon,
				this.materiaal);
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherBonnummer() {
		ReparatiesBon mb = new ReparatiesBon(this.omschrijving, this.bonnummer + ".", this.loon,
				this.materiaal);
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherLoon() {
		ReparatiesBon mb = new ReparatiesBon(this.omschrijving, this.bonnummer, new Geld(3),
				this.materiaal);
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testEqualsFalseOtherMateriaal() {
		ReparatiesBon mb = new ReparatiesBon(this.omschrijving, this.bonnummer, this.loon,
				new Geld(3));
		assertFalse(this.bon.equals(mb));
	}

	@Test
	public void testToString() {
		assertEquals("<ReparatiesBon[omschrijving, bonnummer, <Geld[1,00]>, <Geld[12,00]>]>",
				this.bon.toString());
	}
}
