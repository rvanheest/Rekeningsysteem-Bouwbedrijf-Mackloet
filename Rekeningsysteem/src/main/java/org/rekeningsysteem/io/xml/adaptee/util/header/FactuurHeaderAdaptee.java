package org.rekeningsysteem.io.xml.adaptee.util.header;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.header.Debiteur;
import org.rekeningsysteem.io.xml.adapter.util.header.DatumAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.DebiteurAdapter;

@XmlRootElement(name = "factuurHeader")
@XmlType(propOrder = { "debiteur", "datum", "factuurnummer" })
public class FactuurHeaderAdaptee {

	private Debiteur debiteur;
	private LocalDate datum;
	private String factuurnummer;

	@XmlJavaTypeAdapter(DebiteurAdapter.class)
	public Debiteur getDebiteur() {
		return this.debiteur;
	}

	public void setDebiteur(Debiteur debiteur) {
		this.debiteur = debiteur;
	}

	@XmlJavaTypeAdapter(DatumAdapter.class)
	public LocalDate getDatum() {
		return this.datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	@XmlElement
	public String getFactuurnummer() {
		return this.factuurnummer;
	}

	public void setFactuurnummer(String factuurnummer) {
		this.factuurnummer = factuurnummer;
	}
}
