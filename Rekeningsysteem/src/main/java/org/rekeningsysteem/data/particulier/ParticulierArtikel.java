package org.rekeningsysteem.data.particulier;

import org.rekeningsysteem.data.util.BtwListItem;
import org.rekeningsysteem.data.util.Geld;

public abstract class ParticulierArtikel implements BtwListItem {

	@Override
	public final Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public final double getLoonBtwPercentage() {
		return 0;
	}

	@Override
	public final Geld getLoonBtw() {
		return new Geld(0);
	}
}
