package org.rekeningsysteem.io.xml.adapter.particulier2;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier.ParticulierFactuur2;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.particulier2.ParticulierFactuur2Adaptee;

public class ParticulierFactuur2Adapter extends
		XmlAdapter<ParticulierFactuur2Adaptee, ParticulierFactuur2> {

	@Override
	public ParticulierFactuur2 unmarshal(ParticulierFactuur2Adaptee adaptee) {
		return new ParticulierFactuur2(adaptee.getFactuurHeader(),
				adaptee.getCurrency(), adaptee.getList());
	}

	@Override
	public ParticulierFactuur2Adaptee marshal(ParticulierFactuur2 factuur) {
		ParticulierFactuur2Adaptee adaptee = new ParticulierFactuur2Adaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setCurrency(factuur.getCurrency());
		adaptee.setList(new ItemList<>(factuur.getItemList()));

		return adaptee;
	}

}
