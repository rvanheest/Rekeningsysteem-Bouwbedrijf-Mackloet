package org.rekeningsysteem.data.particulier.loon;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public interface Loon extends ParticulierArtikel {

	// Loon and all its subclasses have no materiaal price, so it is fixed to zero
	@Override
	default Geld materiaal() {
		return new Geld(0);
	}

	@Override
	default BtwPercentage materiaalBtwPercentage() {
		return new BtwPercentage(0, this.loonBtwPercentage().verlegd());
	}

	@Override
	default Geld materiaalBtw() {
		return new Geld(0);
	}
}
