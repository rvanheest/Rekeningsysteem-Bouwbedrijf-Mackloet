package org.rekeningsysteem.io.xml.adaptee.util.header;

import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "naam", "straat", "nummer", "postcode", "plaats", "btwNummer" })
public class DebiteurAdaptee {

	private String naam;
	private String straat;
	private String nummer;
	private String postcode;
	private String plaats;
	private Optional<String> btwNummer;

	public DebiteurAdaptee() {
		this.naam = "";
		this.straat = "";
		this.nummer = "";
		this.postcode = "";
		this.plaats = "";
		this.btwNummer = Optional.of("");
	}

	@XmlElement
	public String getNaam() {
		return this.naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	@XmlElement
	public String getStraat() {
		return this.straat;
	}

	public void setStraat(String straat) {
		this.straat = straat;
	}

	@XmlElement
	public String getNummer() {
		return this.nummer;
	}

	public void setNummer(String nummer) {
		this.nummer = nummer;
	}

	@XmlElement
	public String getPostcode() {
		return this.postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@XmlElement
	public String getPlaats() {
		return this.plaats;
	}

	public void setPlaats(String plaats) {
		this.plaats = plaats;
	}

	@XmlElement
	public Optional<String> getBtwNummer() {
		return this.btwNummer;
	}

	public void setBtwNummer(Optional<String> btwNummer) {
		this.btwNummer = btwNummer;
	}
}
