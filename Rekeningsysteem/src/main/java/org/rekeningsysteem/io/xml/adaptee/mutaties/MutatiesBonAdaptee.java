package org.rekeningsysteem.io.xml.adaptee.mutaties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdaptee;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "mutaties-bon")
@XmlType(propOrder = { "omschrijving", "bonnummer", "prijs" })
public class MutatiesBonAdaptee extends ListItemAdaptee {

	private String omschrijving;
	private String bonnummer;
	private Geld prijs;

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
	public Geld getPrijs() {
		return this.prijs;
	}

	public void setPrijs(Geld prijs) {
		this.prijs = prijs;
	}
}
