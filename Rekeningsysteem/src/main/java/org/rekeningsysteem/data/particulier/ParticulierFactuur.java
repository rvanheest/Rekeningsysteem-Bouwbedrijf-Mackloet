package org.rekeningsysteem.data.particulier;

import org.rekeningsysteem.data.util.Factuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public record ParticulierFactuur(FactuurHeader header, String omschrijving, ItemList<ParticulierArtikel> itemList) implements Factuur<ParticulierArtikel> { }
