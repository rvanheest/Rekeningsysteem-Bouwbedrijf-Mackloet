package org.rekeningsysteem.data.util.visitor;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.EsselinkParticulierArtikel;
import org.rekeningsysteem.data.particulier.ParticulierArtikel2Impl;
import org.rekeningsysteem.data.particulier.loon.InstantLoon2;
import org.rekeningsysteem.data.particulier.loon.ProductLoon2;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;

public interface ListItemVisitor<T> {

	T visit(MutatiesBon item);
	
	T visit(EsselinkParticulierArtikel item);
	
	T visit(ParticulierArtikel2Impl item);
	
	T visit(ReparatiesBon item);
	
	T visit(InstantLoon2 item);
	
	T visit(ProductLoon2 item);
}
