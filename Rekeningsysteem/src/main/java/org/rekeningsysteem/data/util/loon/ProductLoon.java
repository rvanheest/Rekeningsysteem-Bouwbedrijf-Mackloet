package org.rekeningsysteem.data.util.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;

public final class ProductLoon extends AbstractLoon {

	private final double uren;
	private final Geld uurloon;
	private final Geld loon;

	public ProductLoon(String omschrijving, double uren, Geld uurloon) {
		super(omschrijving);
		this.uren = uren;
		this.uurloon = uurloon;
		this.loon = this.uurloon.multiply(this.uren);
	}

	public double getUren() {
		return this.uren;
	}

	public Geld getUurloon() {
		return new Geld(this.uurloon);
	}

	@Override
	public Geld getLoon() {
		return new Geld(this.loon);
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof ProductLoon) {
			ProductLoon that = (ProductLoon) other;
			return Objects.equals(this.uren, that.uren)
					&& Objects.equals(this.uurloon, that.uurloon);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.uren, this.uurloon);
	}

	@Override
	public String toString() {
		return "<ProductLoon[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.uren) + ", "
				+ String.valueOf(this.uurloon) + "]>";
	}
}
