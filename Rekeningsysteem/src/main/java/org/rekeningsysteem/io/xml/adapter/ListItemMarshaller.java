package org.rekeningsysteem.io.xml.adapter; 

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.particulier.AnderArtikel;
import org.rekeningsysteem.data.particulier.GebruiktEsselinkArtikel;
import org.rekeningsysteem.data.particulier.loon.InstantLoon;
import org.rekeningsysteem.data.particulier.loon.ProductLoon;
import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.AnderArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.GebruiktEsselinkArtikelAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.InstantLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.loon.ProductLoonAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;

public final class ListItemMarshaller implements ListItemVisitor<ListItemAdapteeVisitable> {

	@Override
	public MutatiesBonAdaptee visit(MutatiesBon bon) {
		return MutatiesBonAdaptee.build(a -> a
        		.setOmschrijving(bon.getOmschrijving())
        		.setBonnummer(bon.getBonnummer())
        		.setPrijs(bon.getMateriaal()));
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
	public ReparatiesBonAdaptee visit(ReparatiesBon bon) {
		return ReparatiesBonAdaptee.build(a -> a
        		.setOmschrijving(bon.getOmschrijving())
        		.setBonnummer(bon.getBonnummer())
        		.setLoon(bon.getLoon())
        		.setMateriaal(bon.getMateriaal()));
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
