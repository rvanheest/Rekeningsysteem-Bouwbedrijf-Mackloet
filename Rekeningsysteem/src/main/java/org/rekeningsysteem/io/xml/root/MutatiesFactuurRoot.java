package org.rekeningsysteem.io.xml.root;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesFactuur;
import org.rekeningsysteem.io.xml.adapter.RekeningAdapter;

@XmlRootElement(name = "bestand")
public class MutatiesFactuurRoot implements Root<MutatiesFactuur> {

	private MutatiesFactuur factuur;

	@Override
	@XmlAttribute
	public String getType() {
		return "MutatiesFactuur";
	}

	@Override
	@XmlAttribute
	public String getVersion() {
		return "4";
	}

	@Override
	@XmlElementRef
	@XmlJavaTypeAdapter(RekeningAdapter.class)
	public MutatiesFactuur getRekening() {
		return this.factuur;
	}

	@Override
	public void setRekening(MutatiesFactuur factuur) {
		this.factuur = factuur;
	}
}
