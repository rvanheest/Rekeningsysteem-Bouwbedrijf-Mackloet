package org.rekeningsysteem.io.xml.root;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.io.xml.adapter.offerte.OfferteAdapter;

@XmlRootElement(name = "bestand")
public class OfferteRoot implements Root<Offerte> {

	private Offerte offerte;

	@Override
	@XmlAttribute
	public String getType() {
		return "Offerte";
	}

	@Override
	@XmlJavaTypeAdapter(OfferteAdapter.class)
	public Offerte getRekening() {
		return this.offerte;
	}

	@Override
	public void setRekening(Offerte offerte) {
		this.offerte = offerte;
	}
}