package org.rekeningsysteem.data.particulier.materiaal;

import javax.money.MonetaryAmount;

public record EsselinkArtikel(String artikelNummer, String omschrijving, int prijsPer, String eenheid, MonetaryAmount verkoopPrijs) {}
