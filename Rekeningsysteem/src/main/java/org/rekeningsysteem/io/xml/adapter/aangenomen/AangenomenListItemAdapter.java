package org.rekeningsysteem.io.xml.adapter.aangenomen;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.io.xml.adaptee.aangenomen.AangenomenListItemAdaptee;

public class AangenomenListItemAdapter extends
		XmlAdapter<AangenomenListItemAdaptee, AangenomenListItem> {

	@Override
	public AangenomenListItem unmarshal(AangenomenListItemAdaptee adaptee) {
		return new AangenomenListItem(adaptee.getOmschrijving(), adaptee.getLoon(),
				adaptee.getMateriaal());
	}

	@Override
	public AangenomenListItemAdaptee marshal(AangenomenListItem item) {
		AangenomenListItemAdaptee adaptee = new AangenomenListItemAdaptee();
		adaptee.setOmschrijving(item.getOmschrijving());
		adaptee.setLoon(item.getLoon());
		adaptee.setMateriaal(item.getMateriaal());
		return adaptee;
	}
}
