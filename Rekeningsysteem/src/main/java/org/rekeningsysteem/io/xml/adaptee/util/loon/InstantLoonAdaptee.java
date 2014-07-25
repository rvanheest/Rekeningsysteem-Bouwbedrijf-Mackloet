package org.rekeningsysteem.io.xml.adaptee.util.loon;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "instant-loon")
@XmlType(propOrder = { "omschrijving", "loon" })
public class InstantLoonAdaptee extends AbstractLoonAdaptee {

	private String omschrijving;
	private Geld loon;

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
}
