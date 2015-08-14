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
				adaptee.getCurrency(), adaptee.getItemList(), adaptee.getLoonList());
	}

	@Override
	public ParticulierFactuurAdaptee marshal(ParticulierFactuur factuur) {
		ParticulierFactuurAdaptee adaptee = new ParticulierFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setCurrency(factuur.getCurrency());
		adaptee.setItemList(new ItemList<>(factuur.getItemList()));
		adaptee.setLoonList(new ItemList<>(factuur.getLoonList()));

		return adaptee;
	}
}
