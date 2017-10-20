package org.rekeningsysteem.data.util;

import java.util.function.Function;

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class TotalenListItemVisitor implements ListItemVisitor<Function<Totalen, Totalen>> {

	@Override
	public Function<Totalen, Totalen> visit(MutatiesInkoopOrder item) {
		return t -> t.add(item.getMateriaal());
	}

	@Override
	public Function<Totalen, Totalen> visit(GebruiktEsselinkArtikel item) {
		return t -> t.add(item.getMateriaalBtwPercentage(),
				item.getMateriaal(), item.getMateriaalBtw());
	}

	@Override
	public Function<Totalen, Totalen> visit(AnderArtikel item) {
		return t -> t.add(item.getMateriaalBtwPercentage(),
				item.getMateriaal(), item.getMateriaalBtw());
	}

	@Override
	public Function<Totalen, Totalen> visit(ReparatiesInkoopOrder item) {
		return t -> t.add(item.getMateriaal());
	}

	@Override
	public Function<Totalen, Totalen> visit(InstantLoon item) {
		return t -> t.add(item.getLoonBtwPercentage(),
				item.getLoon(), item.getLoonBtw());
	}

	@Override
	public Function<Totalen, Totalen> visit(ProductLoon item) {
		return t -> t.add(item.getLoonBtwPercentage(),
				item.getLoon(), item.getLoonBtw());
	}
}
