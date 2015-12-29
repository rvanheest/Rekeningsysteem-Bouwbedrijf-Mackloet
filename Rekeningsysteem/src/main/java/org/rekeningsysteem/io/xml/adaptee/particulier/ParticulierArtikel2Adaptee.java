package org.rekeningsysteem.io.xml.adaptee.particulier;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoon2Adaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoon2Adaptee;

// TODO ParticulierArtikelAdaptee
@XmlSeeAlso({ ParticulierArtikel2ImplAdaptee.class, EsselinkParticulierArtikelAdaptee.class,
	InstantLoon2Adaptee.class, ProductLoon2Adaptee.class })
public abstract class ParticulierArtikel2Adaptee {
}
