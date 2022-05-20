package org.rekeningsysteem.data.particulier.materiaal;

import org.javamoney.moneta.Money;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.MonetaryAmount;

public interface Materiaal extends ParticulierArtikel {

	// Materiaal and all its subclasses have no loon price, so it is fixed to zero
	@Override
	default MonetaryAmount loon() {
		return Money.zero(this.materiaal().getCurrency());
	}

	@Override
	default BtwPercentage loonBtwPercentage() {
		return new BtwPercentage(0, this.materiaalBtwPercentage().verlegd());
	}

	@Override
	default MonetaryAmount loonBtw() {
		return Money.zero(this.materiaal().getCurrency());
	}
}
