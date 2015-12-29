package org.rekeningsysteem.io.xml.root;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierFactuur2;
import org.rekeningsysteem.io.xml.adapter.particulier2.ParticulierFactuur2Adapter;

@XmlRootElement(name = "bestand")
public class ParticulierFactuur2Root implements Root<ParticulierFactuur2> {

	private ParticulierFactuur2 factuur;

	@Override
	@XmlAttribute
	public String getType() {
		return "ParticulierFactuur";
	}

	@Override
	@XmlAttribute
	public String getVersion() {
		return "4";
	}

	@Override
	@XmlJavaTypeAdapter(ParticulierFactuur2Adapter.class)
	public ParticulierFactuur2 getRekening() {
		return this.factuur;
	}

	@Override
	public void setRekening(ParticulierFactuur2 factuur) {
		this.factuur = factuur;
	}
}
