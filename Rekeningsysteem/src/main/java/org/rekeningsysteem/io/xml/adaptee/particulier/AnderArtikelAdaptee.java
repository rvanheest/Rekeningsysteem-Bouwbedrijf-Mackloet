package org.rekeningsysteem.io.xml.adaptee.particulier;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "ander-artikel")
@XmlType(propOrder = { "omschrijving", "prijs", "materiaalBtwPercentage" })
public class AnderArtikelAdaptee extends ListItemAdapteeVisitable {

	private String omschrijving;
	private Geld prijs;
	private double materiaalBtwPercentage;

	private AnderArtikelAdaptee() {
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public AnderArtikelAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getPrijs() {
		return this.prijs;
	}

	public AnderArtikelAdaptee setPrijs(Geld prijs) {
		this.prijs = prijs;
		return this;
	}

	@XmlElement
	public double getMateriaalBtwPercentage() {
		return this.materiaalBtwPercentage;
	}

	public AnderArtikelAdaptee setMateriaalBtwPercentage(double materiaalBtwPercentage) {
		this.materiaalBtwPercentage = materiaalBtwPercentage;
		return this;
	}

	@Override
	public <T> T accept(ListItemAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static AnderArtikelAdaptee build(
			Function<AnderArtikelAdaptee, AnderArtikelAdaptee> builder) {
		return builder.apply(new AnderArtikelAdaptee());
	}
}
