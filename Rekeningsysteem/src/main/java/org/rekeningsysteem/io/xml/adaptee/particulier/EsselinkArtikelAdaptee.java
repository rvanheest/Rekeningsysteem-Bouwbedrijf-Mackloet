package org.rekeningsysteem.io.xml.adaptee.particulier;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "artikel")
@XmlType(propOrder = { "artikelNummer", "omschrijving", "prijsPer", "eenheid", "verkoopPrijs" })
public class EsselinkArtikelAdaptee {

	private String artikelNummer;
	private String omschrijving;
	private int prijsPer;
	private String eenheid;
	private Geld verkoopPrijs;

	private EsselinkArtikelAdaptee() {
	}

	@XmlElement
	public String getArtikelNummer() {
		return this.artikelNummer;
	}

	public EsselinkArtikelAdaptee setArtikelNummer(String artikelNummer) {
		this.artikelNummer = artikelNummer;
		return this;
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public EsselinkArtikelAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	@XmlElement
	public int getPrijsPer() {
		return this.prijsPer;
	}

	public EsselinkArtikelAdaptee setPrijsPer(int prijsPer) {
		this.prijsPer = prijsPer;
		return this;
	}

	@XmlElement
	public String getEenheid() {
		return this.eenheid;
	}

	public EsselinkArtikelAdaptee setEenheid(String eenheid) {
		this.eenheid = eenheid;
		return this;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getVerkoopPrijs() {
		return this.verkoopPrijs;
	}

	public EsselinkArtikelAdaptee setVerkoopPrijs(Geld verkoopPrijs) {
		this.verkoopPrijs = verkoopPrijs;
		return this;
	}

	public static EsselinkArtikelAdaptee build(
			Function<EsselinkArtikelAdaptee, EsselinkArtikelAdaptee> builder) {
		return builder.apply(new EsselinkArtikelAdaptee());
	}
}
