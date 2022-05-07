package org.rekeningsysteem.data.particulier.materiaal;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public interface Materiaal extends ParticulierArtikel {

	// Materiaal and all its subclasses have no loon price, so it is fixed to zero
	@Override
	default Geld loon() {
		return new Geld(0);
	}

	@Override
	default BtwPercentage loonBtwPercentage() {
		return new BtwPercentage(0, this.materiaalBtwPercentage().verlegd());
	}

	@Override
	default Geld loonBtw() {
		return new Geld(0);
	}
}
