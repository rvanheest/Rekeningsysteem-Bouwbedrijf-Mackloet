package org.rekeningsysteem.io.xml.adapter;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesFactuurAdaptee;

public class RekeningMarshaller implements RekeningVisitor<RekeningAdapteeVisitable> {

	@Override
	public MutatiesFactuurAdaptee visit(MutatiesFactuur factuur) {
		MutatiesFactuurAdaptee adaptee = new MutatiesFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setCurrency(factuur.getCurrency());
		adaptee.setList(new ItemList<>(factuur.getItemList()));
		return adaptee;
	}

	@Override
	public OfferteAdaptee visit(Offerte offerte) {
		OfferteAdaptee adaptee = new OfferteAdaptee();
		adaptee.setFactuurHeader(offerte.getFactuurHeader());
		adaptee.setTekst(offerte.getTekst());
		adaptee.setOndertekenen(offerte.isOndertekenen());
		return adaptee;
	}

	@Override
	public ParticulierFactuurAdaptee visit(ParticulierFactuur factuur) {
		ParticulierFactuurAdaptee adaptee = new ParticulierFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setCurrency(factuur.getCurrency());
		adaptee.setList(new ItemList<>(factuur.getItemList()));
		return adaptee;
	}

	@Override
	public ReparatiesFactuurAdaptee visit(ReparatiesFactuur factuur) {
		ReparatiesFactuurAdaptee adaptee = new ReparatiesFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setCurrency(factuur.getCurrency());
		adaptee.setList(new ItemList<>(factuur.getItemList()));
		return adaptee;
	}
}
