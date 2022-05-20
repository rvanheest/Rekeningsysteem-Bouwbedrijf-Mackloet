package org.rekeningsysteem.test.data.particulier.materiaal;

import org.javamoney.moneta.Money;
import org.rekeningsysteem.data.particulier.materiaal.AnderArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.CurrencyUnit;
import javax.money.Monetary;

public class AnderArtikelTest extends MateriaalTest {

	private final CurrencyUnit currency = Monetary.getCurrency("EUR");

	@Override
	protected AnderArtikel makeInstance() {
		return new AnderArtikel("omschrijving", Money.of(21, this.currency), new BtwPercentage(10, false));
	}

	@Override
	protected AnderArtikel makeInstance(boolean verlegd) {
		return new AnderArtikel("omschrijving", Money.of(21, this.currency), new BtwPercentage(10, verlegd));
	}
}
