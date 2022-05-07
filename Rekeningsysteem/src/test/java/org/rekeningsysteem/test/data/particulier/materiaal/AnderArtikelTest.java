package org.rekeningsysteem.test.data.particulier.materiaal;

import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public class AnderArtikelTest extends MateriaalTest {

	@Override
	protected AnderArtikel makeInstance() {
		return new AnderArtikel("omschrijving", new Geld(21), new BtwPercentage(10, false));
	}

	@Override
	protected AnderArtikel makeInstance(boolean verlegd) {
		return new AnderArtikel("omschrijving", new Geld(21), new BtwPercentage(10, verlegd));
	}
}
