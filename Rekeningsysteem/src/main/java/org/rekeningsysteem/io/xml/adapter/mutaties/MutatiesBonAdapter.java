package org.rekeningsysteem.io.xml.adapter.mutaties;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesBonAdaptee;

public class MutatiesBonAdapter extends XmlAdapter<MutatiesBonAdaptee, MutatiesBon> {

	@Override
	public MutatiesBon unmarshal(MutatiesBonAdaptee adaptee) {
		return new MutatiesBon(adaptee.getOmschrijving(), adaptee.getBonnummer(),
				adaptee.getPrijs());
	}

	@Override
	public MutatiesBonAdaptee marshal(MutatiesBon bon) {
		MutatiesBonAdaptee adaptee = new MutatiesBonAdaptee();
		adaptee.setOmschrijving(bon.getOmschrijving());
		adaptee.setBonnummer(bon.getBonnummer());
		adaptee.setPrijs(bon.getMateriaal());
		return adaptee;
	}
}
