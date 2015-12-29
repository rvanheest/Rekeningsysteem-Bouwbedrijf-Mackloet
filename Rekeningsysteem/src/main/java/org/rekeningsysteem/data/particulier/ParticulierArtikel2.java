package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.BtwListItem;

// TODO ParticulierArtikel
public abstract class ParticulierArtikel2 implements BtwListItem {

	private final String omschrijving;

	public ParticulierArtikel2(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ParticulierArtikel2) {
			ParticulierArtikel2 that = (ParticulierArtikel2) other;
			return Objects.equals(this.omschrijving, that.omschrijving);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving);
	}
}
