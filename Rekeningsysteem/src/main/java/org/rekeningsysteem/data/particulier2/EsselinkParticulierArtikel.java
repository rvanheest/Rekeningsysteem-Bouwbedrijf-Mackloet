package org.rekeningsysteem.data.particulier2;

import java.util.Objects;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class EsselinkParticulierArtikel extends ParticulierArtikel2Impl {

	private final EsselinkArtikel artikel;

	public EsselinkParticulierArtikel(String omschrijving, Geld prijs, double btwPercentage, EsselinkArtikel artikel) {
		super(omschrijving, prijs, btwPercentage);
		this.artikel = artikel;
	}

	public EsselinkArtikel getArtikel() {
		return this.artikel;
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof EsselinkParticulierArtikel) {
			EsselinkParticulierArtikel that = (EsselinkParticulierArtikel) other;
			return super.equals(that)
					&& Objects.equals(this.artikel, that.artikel);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.artikel);
	}

	@Override
	public String toString() {
		return "<EsselinkParticulierArtikel[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.getMateriaal()) + ", "
				+ String.valueOf(this.getMateriaalBtwPercentage()) + ", "
				+ String.valueOf(this.artikel) + "]>";
	}
}
