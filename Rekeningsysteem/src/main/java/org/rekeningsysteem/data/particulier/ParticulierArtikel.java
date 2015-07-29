package org.rekeningsysteem.data.particulier;

import org.rekeningsysteem.data.util.BtwListItem;
import org.rekeningsysteem.data.util.Geld;

public abstract class ParticulierArtikel extends BtwListItem {

	@Override
	public Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public double getLoonBtwPercentage() {
		return 0;
	}

	@Override
	public Geld getTotaal() {
		return this.getMateriaal().add(this.getMateriaalBtw());
	}
}
