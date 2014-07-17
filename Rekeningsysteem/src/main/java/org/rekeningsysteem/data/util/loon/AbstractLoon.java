package org.rekeningsysteem.data.util.loon;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;

public abstract class AbstractLoon {

	private final String omschrijving;

	public AbstractLoon(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public abstract Geld getLoon();

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
