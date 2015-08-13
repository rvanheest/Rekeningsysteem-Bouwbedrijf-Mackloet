package org.rekeningsysteem.data.particulier;

import org.rekeningsysteem.data.util.BtwListItem;
import org.rekeningsysteem.data.util.Geld;

public interface ParticulierArtikel extends BtwListItem {

	@Override
	default Geld getLoon() {
		return new Geld(0);
	}

	@Override
	default double getLoonBtwPercentage() {
		return 0;
	}
}
