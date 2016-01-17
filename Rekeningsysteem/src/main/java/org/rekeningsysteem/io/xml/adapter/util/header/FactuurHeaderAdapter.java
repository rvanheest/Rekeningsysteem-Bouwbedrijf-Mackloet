package org.rekeningsysteem.io.xml.adapter.util.header;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.util.header.FactuurHeaderAdaptee;

public class FactuurHeaderAdapter extends XmlAdapter<FactuurHeaderAdaptee, FactuurHeader> {

	@Override
	public FactuurHeader unmarshal(FactuurHeaderAdaptee adaptee) {
		return new FactuurHeader(adaptee.getDebiteur(), adaptee.getDatum(),
				adaptee.getFactuurnummer());
	}

	@Override
	public FactuurHeaderAdaptee marshal(FactuurHeader header) {
		return FactuurHeaderAdaptee.build(adaptee -> adaptee
        		.setDebiteur(header.getDebiteur())
        		.setDatum(header.getDatum())
        		.setFactuurnummer(header.getFactuurnummer().orElse(null)));
	}
}
