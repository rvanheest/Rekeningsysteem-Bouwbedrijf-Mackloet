package org.rekeningsysteem.test.data.particulier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.test.data.util.BtwListItemTest;

public abstract class ParticulierArtikelTest extends BtwListItemTest {

	private ParticulierArtikel artikel;
	private String omschrijving = "omschrijving";

	@Override
	protected abstract ParticulierArtikel makeInstance();

	protected String getTestOmschrijving() {
		return this.omschrijving;
	}

	@Before
	@Override
	public void setUp() {
		super.setUp();
		this.artikel = this.makeInstance();
	}

	@Test
	public void testGetOmschrijving() {
		assertEquals(this.omschrijving, this.artikel.getOmschrijving());
	}

	@Test
	public void testEqualsFalseOtherOmschrijving() {
		this.omschrijving = "foo";
		assertFalse(this.makeInstance().equals(this.artikel));
	}
}
