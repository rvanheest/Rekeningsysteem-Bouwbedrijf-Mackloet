package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.loon.InstantLoon;
import org.rekeningsysteem.data.util.loon.ProductLoon;

public interface ListItemVisitor<T> {

	T visit(AangenomenListItem item);
	
	T visit(MutatiesBon item);
	
	T visit(GebruiktEsselinkArtikel item);
	
	T visit(AnderArtikel item);
	
	T visit(ReparatiesBon item);
	
	T visit(InstantLoon item);
	
	T visit(ProductLoon item);
}
