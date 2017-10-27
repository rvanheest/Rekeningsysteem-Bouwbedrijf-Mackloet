package org.rekeningsysteem.io.xml.adapter; 

import org.rekeningsysteem.data.mutaties.MutatiesInkoopOrder;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesInkoopOrder;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesInkoopOrderAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesInkoopOrderAdaptee;

public final class ListItemMarshaller implements ListItemVisitor<ListItemAdapteeVisitable> {

	@Override
	public MutatiesInkoopOrderAdaptee visit(MutatiesInkoopOrder order) {
		return MutatiesInkoopOrderAdaptee.build(a -> a
        		.setOmschrijving(order.getOmschrijving())
        		.setBonnummer(order.getInkoopOrderNummer())
        		.setPrijs(order.getMateriaal()));
	}

	@Override
	public GebruiktEsselinkArtikelAdaptee visit(GebruiktEsselinkArtikel artikel) {
		return GebruiktEsselinkArtikelAdaptee.build(a -> a
        		.setOmschrijving(artikel.getOmschrijving())
        		.setArtikel(artikel.getArtikel())
        		.setAantal(artikel.getAantal())
        		.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage()));
	}

	@Override
	public AnderArtikelAdaptee visit(AnderArtikel artikel) {
		return AnderArtikelAdaptee.build(a -> a
        		.setOmschrijving(artikel.getOmschrijving())
        		.setPrijs(artikel.getMateriaal())
        		.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage()));
	}

	@Override
	public ReparatiesInkoopOrderAdaptee visit(ReparatiesInkoopOrder order) {
		return ReparatiesInkoopOrderAdaptee.build(a -> a
        		.setOmschrijving(order.getOmschrijving())
        		.setBonnummer(order.getInkoopOrderNummer())
        		.setLoon(order.getLoon())
        		.setMateriaal(order.getMateriaal()));
	}

	@Override
	public InstantLoonAdaptee visit(InstantLoon loon) {
		return InstantLoonAdaptee.build(a -> a
        		.setOmschrijving(loon.getOmschrijving())
        		.setLoon(loon.getLoon())
        		.setLoonBtwPercentage(loon.getLoonBtwPercentage()));
	}

	@Override
	public ProductLoonAdaptee visit(ProductLoon loon) {
		return ProductLoonAdaptee.build(a -> a
        		.setOmschrijving(loon.getOmschrijving())
        		.setUren(loon.getUren())
        		.setUurloon(loon.getUurloon())
        		.setLoonBtwPercentage(loon.getLoonBtwPercentage()));
	}
}
