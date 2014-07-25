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
		DebiteurAdaptee adaptee = new DebiteurAdaptee();
		adaptee.setNaam(deb.getNaam());
		adaptee.setStraat(deb.getStraat());
		adaptee.setNummer(deb.getNummer());
		adaptee.setPostcode(deb.getPostcode());
		adaptee.setPlaats(deb.getPlaats());
		adaptee.setBtwNummer(deb.getBtwNummer());
		return adaptee;
	}
}
