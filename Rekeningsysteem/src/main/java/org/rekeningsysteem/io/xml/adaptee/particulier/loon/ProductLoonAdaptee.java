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

@XmlRootElement(name = "product-loon")
@XmlType(propOrder = { "omschrijving", "uren", "uurloon", "loonBtwPercentage" })
public class ProductLoonAdaptee extends ListItemAdapteeVisitable {

	private String omschrijving;
	private double uren;
	private Geld uurloon;
	private BtwPercentage loonBtwPercentage;

	private ProductLoonAdaptee() {
	}

	@XmlElement
	public String getOmschrijving() {
		return this.omschrijving;
	}

	public ProductLoonAdaptee setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
		return this;
	}

	@XmlElement
	public double getUren() {
		return this.uren;
	}

	public ProductLoonAdaptee setUren(double uren) {
		this.uren = uren;
		return this;
	}

	@XmlJavaTypeAdapter(GeldAdapter.class)
	public Geld getUurloon() {
		return this.uurloon;
	}

	public ProductLoonAdaptee setUurloon(Geld uurloon) {
		this.uurloon = uurloon;
		return this;
	}

	@XmlJavaTypeAdapter(BtwPercentageAdapter.class)
	public BtwPercentage getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}

	public ProductLoonAdaptee setLoonBtwPercentage(BtwPercentage loonBtwPercentage) {
		this.loonBtwPercentage = loonBtwPercentage;
		return this;
	}

	@Override
	public <T> T accept(ListItemAdapteeVisitor<T> visitor) {
		return visitor.visit(this);
	}

	public static ProductLoonAdaptee build(Function<ProductLoonAdaptee, ProductLoonAdaptee> builder) {
		return builder.apply(new ProductLoonAdaptee());
	}
}
