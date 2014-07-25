package org.rekeningsysteem.io.xml.adaptee.particulier;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlType(propOrder = { "artikelNummer", "omschrijving", "prijsPer", "eenheid", "verkoopPrijs" })
public class EsselinkArtikelAdaptee {

	private String artikelNummer;
	private String omschrijving;
	private int prijsPer;
	private String eenheid;
	private Geld verkoopPrijs;

	@XmlElement
	public String getArtikelNummer() {
		return this.artikelNummer;
	}

	public void setArtikelNummer(String artikelNummer) {
		this.artikelNummer = artikelNummer;
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	@XmlElement
	public int getPrijsPer() {
		return this.prijsPer;
	}

	public void setPrijsPer(int prijsPer) {
		this.prijsPer = prijsPer;
	}

	@XmlElement
	public String getEenheid() {
		return this.eenheid;
	}

	public void setEenheid(String eenheid) {
		this.eenheid = eenheid;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getVerkoopPrijs() {
		return this.verkoopPrijs;
	}

	public void setVerkoopPrijs(Geld verkoopPrijs) {
		this.verkoopPrijs = verkoopPrijs;
	}
}
