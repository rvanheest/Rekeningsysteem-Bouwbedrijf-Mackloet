package org.rekeningsysteem.io.xml.adaptee.particulier2;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.rekeningsysteem.io.xml.adaptee.particulier2.loon.InstantLoon2Adaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier2.loon.ProductLoon2Adaptee;

// TODO ParticulierArtikelAdaptee
@XmlSeeAlso({ ParticulierArtikel2ImplAdaptee.class, EsselinkParticulierArtikelAdaptee.class,
	InstantLoon2Adaptee.class, ProductLoon2Adaptee.class })
public abstract class ParticulierArtikel2Adaptee {
}
