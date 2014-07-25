package org.rekeningsysteem.io.xml.adapter.reparaties;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesBonAdaptee;

public class ReparatiesBonAdapter extends XmlAdapter<ReparatiesBonAdaptee, ReparatiesBon> {

	@Override
	public ReparatiesBon unmarshal(ReparatiesBonAdaptee adaptee) {
		return new ReparatiesBon(adaptee.getOmschrijving(), adaptee.getBonnummer(),
				adaptee.getLoon(), adaptee.getMateriaal());
	}

	@Override
	public ReparatiesBonAdaptee marshal(ReparatiesBon bon) {
		ReparatiesBonAdaptee adaptee = new ReparatiesBonAdaptee();
		adaptee.setOmschrijving(bon.getOmschrijving());
		adaptee.setBonnummer(bon.getBonnummer());
		adaptee.setLoon(bon.getLoon());
		adaptee.setMateriaal(bon.getMateriaal());
		return adaptee;
	}
}
