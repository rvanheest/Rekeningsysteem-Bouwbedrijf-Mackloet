package org.rekeningsysteem.test.io.xml.adaptee.particulier;

import org.junit.Before;
import org.junit.Ignore;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierArtikelAdaptee;

@Ignore
@Deprecated
public abstract class ParticulierArtikelAdapteeTest {

	private ParticulierArtikelAdaptee adaptee;

	protected abstract ParticulierArtikelAdaptee makeInstance();

	protected ParticulierArtikelAdaptee getInstance() {
		return this.adaptee;
	}

	@Before
	public void setUp() {
		this.adaptee = this.makeInstance();
	}
}
