package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.BtwListItem;

public abstract class ParticulierArtikel implements BtwListItem {

	private final String omschrijving;

	public ParticulierArtikel(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ParticulierArtikel that) {
			return Objects.equals(this.omschrijving, that.omschrijving);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving);
	}
}
