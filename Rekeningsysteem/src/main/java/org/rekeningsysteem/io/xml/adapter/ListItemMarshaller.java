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
		MutatiesBonAdaptee adaptee = new MutatiesBonAdaptee();
		adaptee.setOmschrijving(bon.getOmschrijving());
		adaptee.setBonnummer(bon.getBonnummer());
		adaptee.setPrijs(bon.getMateriaal());
		return adaptee;
	}

	@Override
	public GebruiktEsselinkArtikelAdaptee visit(GebruiktEsselinkArtikel artikel) {
		GebruiktEsselinkArtikelAdaptee adaptee = new GebruiktEsselinkArtikelAdaptee();
		adaptee.setOmschrijving(artikel.getOmschrijving());
		adaptee.setArtikel(artikel.getArtikel());
		adaptee.setAantal(artikel.getAantal());
		adaptee.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage());
		return adaptee;
	}

	@Override
	public AnderArtikelAdaptee visit(AnderArtikel artikel) {
		AnderArtikelAdaptee adaptee = new AnderArtikelAdaptee();
		adaptee.setOmschrijving(artikel.getOmschrijving());
		adaptee.setPrijs(artikel.getMateriaal());
		adaptee.setMateriaalBtwPercentage(artikel.getMateriaalBtwPercentage());
		return adaptee;
	}

	@Override
	public ReparatiesBonAdaptee visit(ReparatiesBon bon) {
		ReparatiesBonAdaptee adaptee = new ReparatiesBonAdaptee();
		adaptee.setOmschrijving(bon.getOmschrijving());
		adaptee.setBonnummer(bon.getBonnummer());
		adaptee.setLoon(bon.getLoon());
		adaptee.setMateriaal(bon.getMateriaal());
		return adaptee;
	}

	@Override
	public InstantLoonAdaptee visit(InstantLoon loon) {
		InstantLoonAdaptee adaptee = new InstantLoonAdaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setLoon(loon.getLoon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());

		return adaptee;
	}

	@Override
	public ProductLoonAdaptee visit(ProductLoon loon) {
		ProductLoonAdaptee adaptee = new ProductLoonAdaptee();
		adaptee.setOmschrijving(loon.getOmschrijving());
		adaptee.setUren(loon.getUren());
		adaptee.setUurloon(loon.getUurloon());
		adaptee.setLoonBtwPercentage(loon.getLoonBtwPercentage());
		return adaptee;
	}
}
