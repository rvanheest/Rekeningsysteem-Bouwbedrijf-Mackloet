package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;

public interface ListItemVisitor<T> {

	T visit(MutatiesBon item);
	
	T visit(GebruiktEsselinkArtikel item);
	
	T visit(AnderArtikel item);
	
	T visit(ReparatiesBon item);
	
	T visit(InstantLoon item);
	
	T visit(ProductLoon item);
}
