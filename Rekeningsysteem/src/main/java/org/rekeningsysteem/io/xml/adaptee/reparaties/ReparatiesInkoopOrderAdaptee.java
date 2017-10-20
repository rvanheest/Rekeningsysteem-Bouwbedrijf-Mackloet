package org.rekeningsysteem.io.xml.adaptee.reparaties;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "reparaties-bon")
@XmlType(propOrder = { "omschrijving", "bonnummer", "loon", "materiaal" })
public class ReparatiesInkoopOrderAdaptee extends ListItemAdapteeVisitable {

	private String omschrijving;
	private String bonnummer;
	private Geld loon;
	private Geld materiaal;

	private ReparatiesInkoopOrderAdaptee() {
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public ReparatiesInkoopOrderAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	@XmlElement
	public String getBonnummer() {
		return this.bonnummer;
	}

	public ReparatiesInkoopOrderAdaptee setBonnummer(String bonnummer) {
		this.bonnummer = bonnummer;
		return this;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getLoon() {
		return this.loon;
	}

	public ReparatiesInkoopOrderAdaptee setLoon(Geld loon) {
		this.loon = loon;
		return this;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getMateriaal() {
		return this.materiaal;
	}

	public ReparatiesInkoopOrderAdaptee setMateriaal(Geld materiaal) {
		this.materiaal = materiaal;
		return this;
	}

	@Override
	public <T> T accept(ListItemAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static ReparatiesInkoopOrderAdaptee build(Function<ReparatiesInkoopOrderAdaptee, ReparatiesInkoopOrderAdaptee> builder) {
		return builder.apply(new ReparatiesInkoopOrderAdaptee());
	}
}
