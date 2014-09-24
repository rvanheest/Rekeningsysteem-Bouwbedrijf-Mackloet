package org.rekeningsysteem.io.xml.adapter.reparaties;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesFactuurAdaptee;

public class ReparatiesFactuurAdapter extends
		XmlAdapter<ReparatiesFactuurAdaptee, ReparatiesFactuur> {

	@Override
	public ReparatiesFactuur unmarshal(ReparatiesFactuurAdaptee adaptee) {
		return new ReparatiesFactuur(adaptee.getFactuurHeader(), adaptee.getCurrency(),
				adaptee.getList(), adaptee.getBtwPercentage());
	}

	@Override
	public ReparatiesFactuurAdaptee marshal(ReparatiesFactuur factuur) {
		ReparatiesFactuurAdaptee adaptee = new ReparatiesFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setCurrency(factuur.getCurrency());
		adaptee.setList(new ItemList<>(factuur.getItemList()));
		adaptee.setBtwPercentage(factuur.getBtwPercentage());

		return adaptee;
	}
}
