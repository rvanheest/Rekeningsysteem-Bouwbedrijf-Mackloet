package org.rekeningsysteem.io.xml.root;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierFactuur;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierFactuurAdapter;

@XmlRootElement(name = "bestand")
public class ParticulierFactuurRoot implements Root<ParticulierFactuur> {

	private ParticulierFactuur factuur;

	@Override
	@XmlAttribute
	public String getType() {
		return "ParticulierFactuur";
	}

	@Override
	@XmlJavaTypeAdapter(ParticulierFactuurAdapter.class)
	public ParticulierFactuur getRekening() {
		return this.factuur;
	}

	@Override
	public void setRekening(ParticulierFactuur factuur) {
		this.factuur = factuur;
	}
}
