package org.rekeningsysteem.io.xml.adaptee.particulier.loon;

import java.util.function.Function;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;
import org.rekeningsysteem.io.xml.adapter.util.GeldAdapter;

@XmlRootElement(name = "instant-loon")
@XmlType(propOrder = { "omschrijving", "loon", "loonBtwPercentage" })
public class InstantLoonAdaptee extends ListItemAdapteeVisitable {

	private String omschrijving;
	private Geld loon;
	private BtwPercentage loonBtwPercentage;

	private InstantLoonAdaptee() {
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public InstantLoonAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getLoon() {
		return this.loon;
	}

	public InstantLoonAdaptee setLoon(Geld loon) {
		this.loon = loon;
		return this;
	}

	@XmlJavaTypeAdapter(BtwPercentageAdapter.class)
	public BtwPercentage getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}

	public InstantLoonAdaptee setLoonBtwPercentage(BtwPercentage loonBtwPercentage) {
		this.loonBtwPercentage = loonBtwPercentage;
		return this;
	}

	@Override
	public <T> T accept(ListItemAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static InstantLoonAdaptee build(Function<InstantLoonAdaptee, InstantLoonAdaptee> builder) {
		return builder.apply(new InstantLoonAdaptee());
	}
}
