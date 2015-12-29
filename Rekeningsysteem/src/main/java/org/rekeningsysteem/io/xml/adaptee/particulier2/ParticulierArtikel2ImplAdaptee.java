package org.rekeningsysteem.io.xml.adaptee.particulier2;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "ander-artikel")
@XmlType(propOrder = { "omschrijving", "prijs", "materiaalBtwPercentage" })
public class ParticulierArtikel2ImplAdaptee extends ParticulierArtikel2Adaptee {

	private String omschrijving;
	private Geld prijs;
	private double materiaalBtwPercentage;

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getPrijs() {
		return this.prijs;
	}

	public void setPrijs(Geld prijs) {
		this.prijs = prijs;
	}

	@XmlElement
	public double getMateriaalBtwPercentage() {
		return this.materiaalBtwPercentage;
	}

	public void setMateriaalBtwPercentage(double materiaalBtwPercentage) {
		this.materiaalBtwPercentage = materiaalBtwPercentage;
	}
}
