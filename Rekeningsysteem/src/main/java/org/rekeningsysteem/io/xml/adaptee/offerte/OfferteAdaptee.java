package org.rekeningsysteem.io.xml.adaptee.offerte;

import java.util.function.Function;

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

	private OfferteAdaptee() {
	}

	@XmlJavaTypeAdapter(FactuurHeaderAdapter.class)
	public FactuurHeader getFactuurHeader() {
		return this.factuurHeader;
	}

	public OfferteAdaptee setFactuurHeader(FactuurHeader factuurHeader) {
		this.factuurHeader = factuurHeader;
		return this;
	}

	@XmlElement
	public String getTekst() {
		return this.tekst;
	}

	public OfferteAdaptee setTekst(String tekst) {
		this.tekst = tekst;
		return this;
	}

	@XmlElement
	public boolean getOndertekenen() {
		return this.ondertekenen;
	}

	public OfferteAdaptee setOndertekenen(boolean ondertekenen) {
		this.ondertekenen = ondertekenen;
		return this;
	}

	@Override
	public <T> T accept(RekeningAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static OfferteAdaptee build(Function<OfferteAdaptee, OfferteAdaptee> builder) {
		return builder.apply(new OfferteAdaptee());
	}
}
