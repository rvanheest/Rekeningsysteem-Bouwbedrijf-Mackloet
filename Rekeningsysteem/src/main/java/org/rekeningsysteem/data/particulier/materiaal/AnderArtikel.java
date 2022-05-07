package org.rekeningsysteem.data.particulier.materiaal;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public record AnderArtikel(String omschrijving, Geld materiaal, BtwPercentage materiaalBtwPercentage) implements Materiaal { }
