package org.rekeningsysteem.io.xml.adaptee.offerte;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.RekeningAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.util.header.FactuurHeaderAdapter;

@XmlRootElement(name = "offerte")
@XmlType(propOrder = { "factuurHeader", "tekst", "ondertekenen" })
public class OfferteAdaptee extends RekeningAdapteeVisitable {

	private FactuurHeader factuurHeader;
	private String tekst;
	private boolean ondertekenen;

	@XmlJavaTypeAdapter(FactuurHeaderAdapter.class)
	public FactuurHeader getFactuurHeader() {
		return this.factuurHeader;
	}

	public void setFactuurHeader(FactuurHeader factuurHeader) {
		this.factuurHeader = factuurHeader;
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

	@Override
	public <T> T accept(RekeningAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}
}
