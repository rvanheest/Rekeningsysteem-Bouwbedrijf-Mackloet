package org.rekeningsysteem.io.xml.adaptee.mutaties;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "mutaties-bon")
@XmlType(propOrder = { "omschrijving", "bonnummer", "prijs" })
public class MutatiesInkoopOrderAdaptee extends ListItemAdapteeVisitable {

	private String omschrijving;
	private String bonnummer;
	private Geld prijs;

	private MutatiesInkoopOrderAdaptee() {
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public MutatiesInkoopOrderAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	@XmlElement
	public String getBonnummer() {
		return this.bonnummer;
	}

	public MutatiesInkoopOrderAdaptee setBonnummer(String bonnummer) {
		this.bonnummer = bonnummer;
		return this;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getPrijs() {
		return this.prijs;
	}

	public MutatiesInkoopOrderAdaptee setPrijs(Geld prijs) {
		this.prijs = prijs;
		return this;
	}

	@Override
	public <T> T accept(ListItemAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static MutatiesInkoopOrderAdaptee build(Function<MutatiesInkoopOrderAdaptee, MutatiesInkoopOrderAdaptee> builder) {
		return builder.apply(new MutatiesInkoopOrderAdaptee());
	}
}
