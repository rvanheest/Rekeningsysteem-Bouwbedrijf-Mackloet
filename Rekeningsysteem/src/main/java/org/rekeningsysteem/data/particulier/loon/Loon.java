package org.rekeningsysteem.data.particulier.loon;

import org.javamoney.moneta.Money;
import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.MonetaryAmount;

public interface Loon extends ParticulierArtikel {

	// Loon and all its subclasses have no materiaal price, so it is fixed to zero
	@Override
	default MonetaryAmount materiaal() {
		return Money.zero(this.loon().getCurrency());
	}

	@Override
	default BtwPercentage materiaalBtwPercentage() {
		return new BtwPercentage(0, this.loonBtwPercentage().verlegd());
	}

	@Override
	default MonetaryAmount materiaalBtw() {
		return Money.zero(this.loon().getCurrency());
	}
}
