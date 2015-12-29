package org.rekeningsysteem.io.xml.adaptee.aangenomen;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@Deprecated
@XmlType(propOrder = { "omschrijving", "loon", "loonBtwPercentage", "materiaal",
		"materiaalBtwPercentage" })
public class AangenomenListItemAdaptee {

	private String omschrijving;
	private Geld loon;
	private double loonBtwPercentage;
	private Geld materiaal;
	private double materiaalBtwPercentage;

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getLoon() {
		return this.loon;
	}

	public void setLoon(Geld loon) {
		this.loon = loon;
	}

	@XmlElement
	public double getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}

	public void setLoonBtwPercentage(double loonBtwPercentage) {
		this.loonBtwPercentage = loonBtwPercentage;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getMateriaal() {
		return this.materiaal;
	}

	public void setMateriaal(Geld materiaal) {
		this.materiaal = materiaal;
	}

	@XmlElement
	public double getMateriaalBtwPercentage() {
		return this.materiaalBtwPercentage;
	}

	public void setMateriaalBtwPercentage(double materiaalBtwPercentage) {
		this.materiaalBtwPercentage = materiaalBtwPercentage;
	}
}
