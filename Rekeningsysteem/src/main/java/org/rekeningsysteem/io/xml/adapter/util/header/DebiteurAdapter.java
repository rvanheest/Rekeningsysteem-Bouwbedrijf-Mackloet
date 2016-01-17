package org.rekeningsysteem.io.xml.adapter.util.header;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.xml.adaptee.util.header.DebiteurAdaptee;

public class DebiteurAdapter extends XmlAdapter<DebiteurAdaptee, Debiteur> {

	@Override
	public Debiteur unmarshal(DebiteurAdaptee adaptee) {
		return new Debiteur(adaptee.getNaam(), adaptee.getStraat(), adaptee.getNummer(),
				adaptee.getPostcode(), adaptee.getPlaats(), adaptee.getBtwNummer());
	}

	@Override
	public DebiteurAdaptee marshal(Debiteur deb) {
		return DebiteurAdaptee.build(adaptee -> adaptee
        		.setNaam(deb.getNaam())
        		.setStraat(deb.getStraat())
        		.setNummer(deb.getNummer())
        		.setPostcode(deb.getPostcode())
        		.setPlaats(deb.getPlaats())
        		.setBtwNummer(deb.getBtwNummer().orElse(null)));
	}
}
