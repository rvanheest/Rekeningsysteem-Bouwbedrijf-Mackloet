package org.rekeningsysteem.io.xml.root;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesFactuur;
import org.rekeningsysteem.io.xml.adapter.reparaties.ReparatiesFactuurAdapter;

@XmlRootElement(name = "bestand")
public class ReparatiesFactuurRoot implements Root<ReparatiesFactuur> {

	private ReparatiesFactuur factuur;

	@Override
	@XmlAttribute
	public String getType() {
		return "ReparatiesFactuur";
	}

	@Override
	@XmlAttribute
	public String getVersion() {
		return "4";
	}

	@Override
	@XmlJavaTypeAdapter(ReparatiesFactuurAdapter.class)
	public ReparatiesFactuur getRekening() {
		return this.factuur;
	}

	@Override
	public void setRekening(ReparatiesFactuur factuur) {
		this.factuur = factuur;
	}
}
