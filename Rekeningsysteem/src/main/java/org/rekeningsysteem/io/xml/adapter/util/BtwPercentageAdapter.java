package org.rekeningsysteem.io.xml.adapter.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.io.xml.adaptee.util.BtwPercentageAdaptee;

public class BtwPercentageAdapter extends XmlAdapter<BtwPercentageAdaptee, BtwPercentage> {

	@Override
	public BtwPercentage unmarshal(BtwPercentageAdaptee adaptee) {
		return new BtwPercentage(adaptee.getLoonPercentage(), adaptee.getMateriaalPercentage());
	}

	@Override
	public BtwPercentageAdaptee marshal(BtwPercentage btw) {
		BtwPercentageAdaptee adaptee = new BtwPercentageAdaptee();
		adaptee.setLoonPercentage(btw.getLoonPercentage());
		adaptee.setMateriaalPercentage(btw.getMateriaalPercentage());
		return adaptee;
	}
}
