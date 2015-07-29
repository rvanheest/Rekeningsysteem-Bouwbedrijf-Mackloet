package org.rekeningsysteem.data.util.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.BtwListItem;
import org.rekeningsysteem.data.util.Geld;

public abstract class AbstractLoon extends BtwListItem {

	private final String omschrijving;

	public AbstractLoon(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public Geld getMateriaal() {
		return new Geld(0);
	}

	@Override
	public double getMateriaalBtwPercentage() {
		return 0;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractLoon) {
			AbstractLoon that = (AbstractLoon) other;
			return Objects.equals(this.omschrijving, that.omschrijving);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving);
	}
}
