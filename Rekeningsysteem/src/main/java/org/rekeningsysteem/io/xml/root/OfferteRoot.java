package org.rekeningsysteem.io.xml.root;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.offerte.Offerte;
import org.rekeningsysteem.io.xml.adapter.RekeningAdapter;

@XmlRootElement(name = "bestand")
public class OfferteRoot implements Root<Offerte> {

	private Offerte offerte;

	private OfferteRoot() {
	}

	@Override
	@XmlAttribute
	public String getType() {
		return "Offerte";
	}

	@Override
	@XmlAttribute
	public String getVersion() {
		return "4";
	}

	@Override
	@XmlElementRef
	@XmlJavaTypeAdapter(RekeningAdapter.class)
	public Offerte getRekening() {
		return this.offerte;
	}

	@Override
	public OfferteRoot setRekening(Offerte offerte) {
		this.offerte = offerte;
		return this;
	}

	public static OfferteRoot build(Function<OfferteRoot, OfferteRoot> builder) {
		return builder.apply(new OfferteRoot());
	}
}
