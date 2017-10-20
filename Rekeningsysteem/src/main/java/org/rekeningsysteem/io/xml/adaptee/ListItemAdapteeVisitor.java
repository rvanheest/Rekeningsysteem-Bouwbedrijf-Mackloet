package org.rekeningsysteem.io.xml.adaptee;

import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesInkoopOrderAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesInkoopOrderAdaptee;

public interface ListItemAdapteeVisitor<T> {

	T visit(MutatiesInkoopOrderAdaptee item);
	
	T visit(GebruiktEsselinkArtikelAdaptee item);
	
	T visit(AnderArtikelAdaptee item);
	
	T visit(ReparatiesInkoopOrderAdaptee item);
	
	T visit(InstantLoonAdaptee item);
	
	T visit(ProductLoonAdaptee item);
}
