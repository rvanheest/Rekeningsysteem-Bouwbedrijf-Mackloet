package org.rekeningsysteem.io.xml.adapter.aangenomen;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.aangenomen.AangenomenFactuurAdaptee;

public class AangenomenFactuurAdapter extends
		XmlAdapter<AangenomenFactuurAdaptee, AangenomenFactuur> {

	@Override
	public AangenomenFactuur unmarshal(AangenomenFactuurAdaptee adaptee) {
		return new AangenomenFactuur(adaptee.getFactuurHeader(), adaptee.getValuta(),
				adaptee.getList(), adaptee.getBtwPercentage());
	}

	@Override
	public AangenomenFactuurAdaptee marshal(AangenomenFactuur factuur) {
		AangenomenFactuurAdaptee adaptee = new AangenomenFactuurAdaptee();
		adaptee.setFactuurHeader(factuur.getFactuurHeader());
		adaptee.setValuta(factuur.getValuta());
		adaptee.setList(new ItemList<>(factuur.getItemList()));
		adaptee.setBtwPercentage(factuur.getBtwPercentage());

		return adaptee;
	}
}