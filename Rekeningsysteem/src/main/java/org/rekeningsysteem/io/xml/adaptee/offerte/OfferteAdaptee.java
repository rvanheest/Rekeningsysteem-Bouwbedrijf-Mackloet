package org.rekeningsysteem.io.xml.adaptee.offerte;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adapter.util.header.FactuurHeaderAdapter;

@XmlType(propOrder = { "factuurHeader", "tekst", "ondertekenen" })
public class OfferteAdaptee {

	private FactuurHeader header;
	private String tekst;
	private boolean ondertekenen;

	@XmlJavaTypeAdapter(FactuurHeaderAdapter.class)
	public FactuurHeader getFactuurHeader() {
		return this.header;
	}

	public void setFactuurHeader(FactuurHeader header) {
		this.header = header;
	}

	@XmlElement
	public String getTekst() {
		return this.tekst;
	}

	public void setTekst(String tekst) {
		this.tekst = tekst;
	}

	@XmlElement
	public boolean getOndertekenen() {
		return this.ondertekenen;
	}

	public void setOndertekenen(boolean ondertekenen) {
		this.ondertekenen = ondertekenen;
	}
}
