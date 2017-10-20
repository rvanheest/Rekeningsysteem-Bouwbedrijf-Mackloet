package org.rekeningsysteem.io.xml.adaptee;

import javax.xml.bind.annotation.XmlSeeAlso;

import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesInkoopOrderAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesInkoopOrderAdaptee;

@XmlSeeAlso({ MutatiesInkoopOrderAdaptee.class, ReparatiesInkoopOrderAdaptee.class, AnderArtikelAdaptee.class,
		GebruiktEsselinkArtikelAdaptee.class, InstantLoonAdaptee.class, ProductLoonAdaptee.class })
public abstract class ListItemAdapteeVisitable {

	public abstract <T> T accept(ListItemAdapteeVisitor<T> visitor);
}
