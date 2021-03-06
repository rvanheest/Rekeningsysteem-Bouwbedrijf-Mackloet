package org.rekeningsysteem.data.particulier.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class ProductLoon extends AbstractLoon {

	private final double uren;
	private final Geld uurloon;
	private final Geld loon;
	private final BtwPercentage loonBtwPercentage;

	public ProductLoon(String omschrijving, double uren, Geld uurloon, BtwPercentage loonBtwPercentage) {
		super(omschrijving);
		this.uren = uren;
		this.uurloon = uurloon;
		this.loon = this.uurloon.multiply(this.uren);
		this.loonBtwPercentage = loonBtwPercentage;
	}

	public double getUren() {
		return this.uren;
	}

	public Geld getUurloon() {
		return this.uurloon;
	}

	@Override
	public Geld getLoon() {
		return this.loon;
	}

	@Override
	public final BtwPercentage getMateriaalBtwPercentage() {
		return new BtwPercentage(0, this.loonBtwPercentage.isVerlegd());
	}

	@Override
	public BtwPercentage getLoonBtwPercentage() {
		return this.loonBtwPercentage;
	}
	
	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof ProductLoon) {
			ProductLoon that = (ProductLoon) other;
			return Objects.equals(this.uren, that.uren)
					&& Objects.equals(this.uurloon, that.uurloon)
					&& Objects.equals(this.loonBtwPercentage, that.loonBtwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.uren, this.uurloon, this.loonBtwPercentage);
	}

	@Override
	public String toString() {
		return "<ProductLoon[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.uren) + ", "
				+ String.valueOf(this.uurloon) + ", "
				+ String.valueOf(this.loonBtwPercentage) + "]>";
	}
}
