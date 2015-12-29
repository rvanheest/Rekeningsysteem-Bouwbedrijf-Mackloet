package org.rekeningsysteem.io.xml.adaptee.particulier2.loon;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.particulier2.ParticulierArtikel2Adaptee;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "product-loon")
@XmlType(propOrder = { "omschrijving", "uren", "uurloon", "loonBtwPercentage" })
public class ProductLoon2Adaptee extends ParticulierArtikel2Adaptee {

	private String omschrijving;
	private double uren;
	private Geld uurloon;
	private double loonBtwPercentage;

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	@XmlElement
	public double getUren() {
		return this.uren;
	}

	public void setUren(double uren) {
		this.uren = uren;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getUurloon() {
		return this.uurloon;
	}

	public void setUurloon(Geld uurloon) {
		this.uurloon = uurloon;
	}

	@XmlElement
	public double getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}

	public void setLoonBtwPercentage(double loonBtwPercentage) {
		this.loonBtwPercentage = loonBtwPercentage;
	}
}
