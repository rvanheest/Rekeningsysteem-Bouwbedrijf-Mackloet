package org.rekeningsysteem.io.xml.adapter.aangenomen;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.io.xml.adaptee.aangenomen.AangenomenListItemAdaptee;

public class AangenomenListItemAdapter extends
		XmlAdapter<AangenomenListItemAdaptee, AangenomenListItem> {

	@Override
	public AangenomenListItem unmarshal(AangenomenListItemAdaptee adaptee) {
		return new AangenomenListItem(adaptee.getOmschrijving(), adaptee.getLoon(),
				adaptee.getLoonBtwPercentage(), adaptee.getMateriaal(),
				adaptee.getMateriaalBtwPercentage());
	}

	@Override
	public AangenomenListItemAdaptee marshal(AangenomenListItem item) {
		AangenomenListItemAdaptee adaptee = new AangenomenListItemAdaptee();
		adaptee.setOmschrijving(item.getOmschrijving());
		adaptee.setLoon(item.getLoon());
		adaptee.setLoonBtwPercentage(item.getLoonBtwPercentage());
		adaptee.setMateriaal(item.getMateriaal());
		adaptee.setMateriaalBtwPercentage(item.getMateriaalBtwPercentage());
		return adaptee;
	}
}
