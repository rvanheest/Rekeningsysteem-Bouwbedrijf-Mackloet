package org.rekeningsysteem.io.xml.adapter.offerte;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.io.xml.adaptee.offerte.OfferteAdaptee;

public class OfferteAdapter extends XmlAdapter<OfferteAdaptee, Offerte> {

	@Override
	public Offerte unmarshal(OfferteAdaptee adaptee) {
		return new Offerte(adaptee.getFactuurHeader(), adaptee.getTekst(),
				adaptee.getOndertekenen());
	}

	@Override
	public OfferteAdaptee marshal(Offerte offerte) {
		OfferteAdaptee adaptee = new OfferteAdaptee();
		adaptee.setFactuurHeader(offerte.getFactuurHeader());
		adaptee.setTekst(offerte.getTekst());
		adaptee.setOndertekenen(offerte.isOndertekenen());
		return adaptee;
	}
}
