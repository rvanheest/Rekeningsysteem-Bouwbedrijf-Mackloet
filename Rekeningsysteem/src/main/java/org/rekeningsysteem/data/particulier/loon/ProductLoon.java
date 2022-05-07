package org.rekeningsysteem.data.particulier.loon;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public record ProductLoon(String omschrijving, double uren, Geld uurloon, BtwPercentage loonBtwPercentage) implements Loon {

	@Override
	public Geld loon() {
		return this.uurloon.multiply(this.uren);
	}
}
