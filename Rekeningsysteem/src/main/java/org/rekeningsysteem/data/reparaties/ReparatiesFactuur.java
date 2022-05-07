package org.rekeningsysteem.data.reparaties;

import org.rekeningsysteem.data.util.Factuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public record ReparatiesFactuur(FactuurHeader header, ItemList<ReparatiesInkoopOrder> itemList) implements Factuur<ReparatiesInkoopOrder> { }
