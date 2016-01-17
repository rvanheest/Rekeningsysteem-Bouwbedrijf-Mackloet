package org.rekeningsysteem.io.xml.adaptee.util.header;

import java.time.LocalDate;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.xml.adapter.util.header.DatumAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.DebiteurAdapter;

@XmlRootElement(name = "factuurHeader")
@XmlType(propOrder = { "debiteur", "datum", "factuurnummer", "omschrijving" })
public class OmschrFactuurHeaderAdaptee {

	private Debiteur debiteur;
	private LocalDate datum;
	private String factuurnummer;
	private String omschrijving;

	private OmschrFactuurHeaderAdaptee() {
	}

	@XmlJavaTypeAdapter(DebiteurAdapter.class)
	public Debiteur getDebiteur() {
		return this.debiteur;
	}

	public OmschrFactuurHeaderAdaptee setDebiteur(Debiteur debiteur) {
		this.debiteur = debiteur;
		return this;
	}

	@XmlJavaTypeAdapter(DatumAdapter.class)
	public LocalDate getDatum() {
		return this.datum;
	}

	public OmschrFactuurHeaderAdaptee setDatum(LocalDate datum) {
		this.datum = datum;
		return this;
	}

	@XmlElement
	public String getFactuurnummer() {
		return this.factuurnummer;
	}

	public OmschrFactuurHeaderAdaptee setFactuurnummer(String factuurnummer) {
		this.factuurnummer = factuurnummer;
		return this;
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public OmschrFactuurHeaderAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	public static OmschrFactuurHeaderAdaptee build(
			Function<OmschrFactuurHeaderAdaptee, OmschrFactuurHeaderAdaptee> builder) {
		return builder.apply(new OmschrFactuurHeaderAdaptee());
	}
}
