package org.rekeningsysteem.io.xml.adaptee;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesFactuurAdaptee;

@XmlSeeAlso({ MutatiesFactuurAdaptee.class, OfferteAdaptee.class, ParticulierFactuurAdaptee.class,
	ReparatiesFactuurAdaptee.class })
public abstract class RekeningAdapteeVisitable {

	public abstract <T> T accept(RekeningAdapteeVisitor<T> visitor);
}
