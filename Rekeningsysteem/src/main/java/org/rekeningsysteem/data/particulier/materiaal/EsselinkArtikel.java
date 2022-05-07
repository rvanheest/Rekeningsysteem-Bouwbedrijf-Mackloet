package org.rekeningsysteem.data.particulier.materiaal;

import org.rekeningsysteem.data.util.Geld;

public record EsselinkArtikel(String artikelNummer, String omschrijving, int prijsPer, String eenheid, Geld verkoopPrijs) {}
