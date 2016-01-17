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
@XmlType(propOrder = { "debiteur", "datum", "factuurnummer" })
public class FactuurHeaderAdaptee {

	private Debiteur debiteur;
	private LocalDate datum;
	private String factuurnummer;

	private FactuurHeaderAdaptee() {
	}

	@XmlJavaTypeAdapter(DebiteurAdapter.class)
	public Debiteur getDebiteur() {
		return this.debiteur;
	}

	public FactuurHeaderAdaptee setDebiteur(Debiteur debiteur) {
		this.debiteur = debiteur;
		return this;
	}

	@XmlJavaTypeAdapter(DatumAdapter.class)
	public LocalDate getDatum() {
		return this.datum;
	}

	public FactuurHeaderAdaptee setDatum(LocalDate datum) {
		this.datum = datum;
		return this;
	}

	@XmlElement
	public String getFactuurnummer() {
		return this.factuurnummer;
	}

	public FactuurHeaderAdaptee setFactuurnummer(String factuurnummer) {
		this.factuurnummer = factuurnummer;
		return this;
	}

	public static FactuurHeaderAdaptee build(
			Function<FactuurHeaderAdaptee, FactuurHeaderAdaptee> builder) {
		return builder.apply(new FactuurHeaderAdaptee());
	}
}
