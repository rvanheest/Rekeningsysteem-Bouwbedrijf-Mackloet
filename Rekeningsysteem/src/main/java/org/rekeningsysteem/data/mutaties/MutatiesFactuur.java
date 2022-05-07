package org.rekeningsysteem.data.mutaties;

import org.rekeningsysteem.data.util.Factuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public record MutatiesFactuur(FactuurHeader header, ItemList<MutatiesInkoopOrder> itemList) implements Factuur<MutatiesInkoopOrder> { }
