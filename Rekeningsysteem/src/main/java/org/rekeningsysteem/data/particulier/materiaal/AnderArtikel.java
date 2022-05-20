package org.rekeningsysteem.data.particulier.materiaal;

import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.MonetaryAmount;

public record AnderArtikel(String omschrijving, MonetaryAmount materiaal, BtwPercentage materiaalBtwPercentage) implements Materiaal { }
