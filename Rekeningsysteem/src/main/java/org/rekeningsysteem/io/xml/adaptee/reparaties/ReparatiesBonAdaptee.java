package org.rekeningsysteem.io.xml.adaptee.reparaties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlType(propOrder = { "omschrijving", "bonnummer", "loon", "materiaal" })
public class ReparatiesBonAdaptee {

	private String omschrijving;
	private String bonnummer;
	private Geld loon;
	private Geld materiaal;

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	@XmlElement
	public String getBonnummer() {
		return this.bonnummer;
	}

	public void setBonnummer(String bonnummer) {
		this.bonnummer = bonnummer;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getLoon() {
		return this.loon;
	}

	public void setLoon(Geld loon) {
		this.loon = loon;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getMateriaal() {
		return this.materiaal;
	}

	public void setMateriaal(Geld materiaal) {
		this.materiaal = materiaal;
	}
}
