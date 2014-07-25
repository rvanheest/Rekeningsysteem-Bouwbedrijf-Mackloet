package org.rekeningsysteem.io.xml.adapter.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.util.GeldAdaptee;

public class GeldAdapter extends XmlAdapter<GeldAdaptee, Geld> {

	@Override
	public Geld unmarshal(GeldAdaptee adaptee) {
		return new Geld(adaptee.getBedrag());
	}

	@Override
	public GeldAdaptee marshal(Geld geld) {
		GeldAdaptee adaptee = new GeldAdaptee();
		adaptee.setBedrag(geld.getBedrag());
		return adaptee;
	}
}
