package org.rekeningsysteem.data.particulier.loon;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.Geld;

public abstract class AbstractLoon extends ParticulierArtikel {

	public AbstractLoon(String omschrijving) {
		super(omschrijving);
	}

	// AbstractLoon and all its subclasses have no materiaal price, so it is fixed to zero
	@Override
	public final Geld getMateriaal() {
		return new Geld(0);
	}

	@Override
	public final double getMateriaalBtwPercentage() {
		return 0;
	}

	@Override
	public final Geld getMateriaalBtw() {
		return new Geld(0);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractLoon) {
			AbstractLoon that = (AbstractLoon) other;
			return super.equals(that);
		}
		return false;
	}
}
