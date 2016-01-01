package org.rekeningsysteem.io.xml.adaptee.particulier.loon;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdaptee;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "instant-loon")
@XmlType(propOrder = { "omschrijving", "loon", "loonBtwPercentage" })
public class InstantLoonAdaptee extends ListItemAdaptee {

	private String omschrijving;
	private Geld loon;
	private double loonBtwPercentage;

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
}
