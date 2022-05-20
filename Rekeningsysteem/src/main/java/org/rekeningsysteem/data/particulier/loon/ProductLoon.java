package org.rekeningsysteem.data.particulier.loon;

import org.rekeningsysteem.data.util.BtwPercentage;

import javax.money.MonetaryAmount;

public record ProductLoon(String omschrijving, double uren, MonetaryAmount uurloon, BtwPercentage loonBtwPercentage) implements Loon {

	@Override
	public MonetaryAmount loon() {
		return this.uurloon.multiply(this.uren);
	}
}
