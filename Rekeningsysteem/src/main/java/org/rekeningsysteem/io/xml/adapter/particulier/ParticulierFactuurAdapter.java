package org.rekeningsysteem.io.xml.adapter.particulier;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierFactuurAdaptee;

public class ParticulierFactuurAdapter extends
		XmlAdapter<ParticulierFactuurAdaptee, ParticulierFactuur> {

	@Override
	public ParticulierFactuur unmarshal(ParticulierFactuurAdaptee adaptee) {
		return new ParticulierFactuur(adaptee.getFactuurHeader(),
				adaptee.getValuta(), adaptee.getItemList(), adaptee.getLoonList(),
				adaptee.getBtwPercentage());
	}

	@Override
	public ParticulierFactuurAdaptee marshal(ParticulierFactuur factuur) {
		ParticulierFactuurAdaptee adaptee = new ParticulierFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setValuta(factuur.getValuta());
		adaptee.setItemList(new ItemList<>(factuur.getItemList()));
		adaptee.setLoonList(new ItemList<>(factuur.getLoonList()));
		adaptee.setBtwPercentage(factuur.getBtwPercentage());

		return adaptee;
	}
}
