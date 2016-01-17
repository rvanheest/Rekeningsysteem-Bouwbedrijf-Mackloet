package org.rekeningsysteem.io.xml.adapter.util.header;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.util.header.OmschrFactuurHeaderAdaptee;

public class OmschrFactuurHeaderAdapter extends
		XmlAdapter<OmschrFactuurHeaderAdaptee, OmschrFactuurHeader> {

	@Override
	public OmschrFactuurHeader unmarshal(OmschrFactuurHeaderAdaptee adaptee) {
		return new OmschrFactuurHeader(adaptee.getDebiteur(), adaptee.getDatum(),
				adaptee.getFactuurnummer(), adaptee.getOmschrijving());
	}

	@Override
	public OmschrFactuurHeaderAdaptee marshal(OmschrFactuurHeader header) {
		return OmschrFactuurHeaderAdaptee.build(adaptee -> adaptee
        		.setDebiteur(header.getDebiteur())
        		.setDatum(header.getDatum())
        		.setFactuurnummer(header.getFactuurnummer().orElse(null))
        		.setOmschrijving(header.getOmschrijving()));
	}
}
