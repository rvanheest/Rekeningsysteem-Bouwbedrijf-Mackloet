package org.rekeningsysteem.io.xml.adapter.mutaties;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesFactuurAdaptee;

public class MutatiesFactuurAdapter extends XmlAdapter<MutatiesFactuurAdaptee, MutatiesFactuur> {

	@Override
	public MutatiesFactuur unmarshal(MutatiesFactuurAdaptee adaptee) {
		return new MutatiesFactuur(adaptee.getFactuurHeader(), adaptee.getValuta(),
				adaptee.getList(), adaptee.getBtwPercentage());
	}

	@Override
	public MutatiesFactuurAdaptee marshal(MutatiesFactuur factuur) {
		MutatiesFactuurAdaptee adaptee = new MutatiesFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setValuta(factuur.getValuta());
		adaptee.setList(new ItemList<>(factuur.getItemList()));
		adaptee.setBtwPercentage(factuur.getBtwPercentage());

		return adaptee;
	}
}
