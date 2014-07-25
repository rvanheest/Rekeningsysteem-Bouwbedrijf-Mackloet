package org.rekeningsysteem.io.xml.root;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenFactuur;
import org.rekeningsysteem.io.xml.adapter.aangenomen.AangenomenFactuurAdapter;

@XmlRootElement(name = "bestand")
public class AangenomenFactuurRoot implements Root<AangenomenFactuur> {

	private AangenomenFactuur factuur;

	@Override
	@XmlAttribute
	public String getType() {
		return "AangenomenFactuur";
	}

	@Override
	@XmlJavaTypeAdapter(AangenomenFactuurAdapter.class)
	public AangenomenFactuur getRekening() {
		return this.factuur;
	}

	@Override
	public void setRekening(AangenomenFactuur rekening) {
		this.factuur = rekening;
	}
}
