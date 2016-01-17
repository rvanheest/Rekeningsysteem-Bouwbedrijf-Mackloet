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
		return MutatiesFactuurAdaptee.build(a -> a
        		.setFactuurHeader(factuur.getFactuurHeader())
        		.setCurrency(factuur.getCurrency())
        		.setList(new ItemList<>(factuur.getItemList())));
	}

	@Override
	public OfferteAdaptee visit(Offerte offerte) {
		return OfferteAdaptee.build(a -> a
        		.setFactuurHeader(offerte.getFactuurHeader())
        		.setTekst(offerte.getTekst())
        		.setOndertekenen(offerte.isOndertekenen()));
	}

	@Override
	public ParticulierFactuurAdaptee visit(ParticulierFactuur factuur) {
		return ParticulierFactuurAdaptee.build(a -> a
        		.setFactuurHeader(factuur.getFactuurHeader())
        		.setCurrency(factuur.getCurrency())
        		.setList(new ItemList<>(factuur.getItemList())));
	}

	@Override
	public ReparatiesFactuurAdaptee visit(ReparatiesFactuur factuur) {
		return ReparatiesFactuurAdaptee.build(a -> a
        		.setFactuurHeader(factuur.getFactuurHeader())
        		.setCurrency(factuur.getCurrency())
        		.setList(new ItemList<>(factuur.getItemList())));
	}
}
