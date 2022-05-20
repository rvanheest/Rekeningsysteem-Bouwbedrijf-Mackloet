package org.rekeningsysteem.data.particulier.materiaal;

import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.MonetaryAmount;

public record GebruiktEsselinkArtikel(String omschrijving, EsselinkArtikel artikel, double aantal, BtwPercentage materiaalBtwPercentage) implements Materiaal {

	public GebruiktEsselinkArtikel(EsselinkArtikel artikel, double aantal, BtwPercentage btwPercentage) {
		this(artikel.omschrijving(), artikel, aantal, btwPercentage);
	}

	@Override
	public MonetaryAmount materiaal() {
		return this.artikel.verkoopPrijs().multiply(this.aantal).divide(this.artikel.prijsPer());
	}
}
