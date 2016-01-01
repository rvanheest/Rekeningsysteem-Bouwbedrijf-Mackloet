package org.rekeningsysteem.io.xml.adaptee;

import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;

public interface ListItemAdapteeVisitor<T> {

	T visit(MutatiesBonAdaptee item);
	
	T visit(GebruiktEsselinkArtikelAdaptee item);
	
	T visit(AnderArtikelAdaptee item);
	
	T visit(ReparatiesBonAdaptee item);
	
	T visit(InstantLoonAdaptee item);
	
	T visit(ProductLoonAdaptee item);
}
