package org.rekeningsysteem.io.xml.adaptee.particulier;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;

@XmlSeeAlso({ AnderArtikelAdaptee.class, GebruiktEsselinkArtikelAdaptee.class,
	InstantLoonAdaptee.class, ProductLoonAdaptee.class })
public abstract class ParticulierArtikelAdaptee {
}
