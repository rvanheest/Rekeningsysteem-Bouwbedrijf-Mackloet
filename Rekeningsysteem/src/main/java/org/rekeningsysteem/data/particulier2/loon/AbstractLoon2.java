package org.rekeningsysteem.data.particulier2.loon;

import java.util.Objects;

import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.data.util.Geld;

// TODO AbstractLoon
public abstract class AbstractLoon2 implements ParticulierArtikel2 {

	private final String omschrijving;

	public AbstractLoon2(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	// AbstractLoon and all its subclasses have no materiaal price, so it is fixed to zero
	@Override
	public final Geld getMateriaal() {
		return new Geld(0);
	}

	@Override
	public final double getMateriaalBtwPercentage() {
		return 0;
	}

	@Override
	public final Geld getMateriaalBtw() {
		return new Geld(0);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractLoon2) {
			AbstractLoon2 that = (AbstractLoon2) other;
			return Objects.equals(this.omschrijving, that.omschrijving);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving);
	}
}
