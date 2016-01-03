package org.rekeningsysteem.io.xml.adaptee;

import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesFactuurAdaptee;

public interface RekeningAdapteeVisitor<T> {

	T visit(MutatiesFactuurAdaptee item);

	T visit(OfferteAdaptee item);

	T visit(ParticulierFactuurAdaptee item);

	T visit(ReparatiesFactuurAdaptee item);
}
