package org.rekeningsysteem.io.xml.adaptee.util.header;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "debiteur")
@XmlType(propOrder = { "naam", "straat", "nummer", "postcode", "plaats", "btwNummer" })
public class DebiteurAdaptee {

	private String naam;
	private String straat;
	private String nummer;
	private String postcode;
	private String plaats;
	private String btwNummer;

	private DebiteurAdaptee() {
	}

	@XmlElement
	public String getNaam() {
		return this.naam;
	}

	public DebiteurAdaptee setNaam(String naam) {
		this.naam = naam;
		return this;
	}

	@XmlElement
	public String getStraat() {
		return this.straat;
	}

	public DebiteurAdaptee setStraat(String straat) {
		this.straat = straat;
		return this;
	}

	@XmlElement
	public String getNummer() {
		return this.nummer;
	}

	public DebiteurAdaptee setNummer(String nummer) {
		this.nummer = nummer;
		return this;
	}

	@XmlElement
	public String getPostcode() {
		return this.postcode;
	}

	public DebiteurAdaptee setPostcode(String postcode) {
		this.postcode = postcode;
		return this;
	}

	@XmlElement
	public String getPlaats() {
		return this.plaats;
	}

	public DebiteurAdaptee setPlaats(String plaats) {
		this.plaats = plaats;
		return this;
	}

	@XmlElement
	public String getBtwNummer() {
		return this.btwNummer;
	}

	public DebiteurAdaptee setBtwNummer(String btwNummer) {
		this.btwNummer = btwNummer;
		return this;
	}

	public static DebiteurAdaptee build(Function<DebiteurAdaptee, DebiteurAdaptee> builder) {
		return builder.apply(new DebiteurAdaptee());
	}
}
