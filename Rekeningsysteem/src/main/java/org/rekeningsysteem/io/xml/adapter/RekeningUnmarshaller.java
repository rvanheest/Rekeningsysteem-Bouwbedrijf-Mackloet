package org.rekeningsysteem.io.xml.adapter;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesFactuurAdaptee;

public class RekeningUnmarshaller implements RekeningAdapteeVisitor<AbstractRekening> {

	@Override
	public MutatiesFactuur visit(MutatiesFactuurAdaptee adaptee) {
		return new MutatiesFactuur(adaptee.getFactuurHeader(), adaptee.getCurrency(),
				adaptee.getList());
	}

	@Override
	public Offerte visit(OfferteAdaptee adaptee) {
		return new Offerte(adaptee.getFactuurHeader(), adaptee.getTekst(),
				adaptee.getOndertekenen());
	}

	@Override
	public ParticulierFactuur visit(ParticulierFactuurAdaptee adaptee) {
		return new ParticulierFactuur(adaptee.getFactuurHeader(),
				adaptee.getCurrency(), adaptee.getList());
	}

	@Override
	public ReparatiesFactuur visit(ReparatiesFactuurAdaptee adaptee) {
		return new ReparatiesFactuur(adaptee.getFactuurHeader(), adaptee.getCurrency(),
				adaptee.getList());
	}

}
