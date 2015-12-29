package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import org.junit.Before;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierArtikel2Adaptee;

public abstract class ParticulierArtikel2AdapteeTest {

	private ParticulierArtikel2Adaptee adaptee;

	protected abstract ParticulierArtikel2Adaptee makeInstance();

	protected ParticulierArtikel2Adaptee getInstance() {
		return this.adaptee;
	}

	@Before
	public void setUp() {
		this.adaptee = this.makeInstance();
	}
}
