package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class GebruiktEsselinkArtikel implements ParticulierArtikel {

	private final EsselinkArtikel artikel;
	private final double aantal;
	private final Geld materiaal;

	public GebruiktEsselinkArtikel(EsselinkArtikel artikel, double aantal) {
		this.artikel = artikel;
		this.aantal = aantal;
		this.materiaal = this.artikel.getVerkoopPrijs().multiply(this.aantal)
				.divide(this.artikel.getPrijsPer());
	}

	public EsselinkArtikel getArtikel() {
		return this.artikel;
	}

	public double getAantal() {
		return this.aantal;
	}

	@Override
	public Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public Geld getMateriaal() {
		return new Geld(this.materiaal);
	}

	@Override
	public Geld getTotaal() {
		return this.getMateriaal();
	}

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof GebruiktEsselinkArtikel) {
			GebruiktEsselinkArtikel that = (GebruiktEsselinkArtikel) other;
			return Objects.equals(this.artikel, that.artikel)
					&& Objects.equals(this.aantal, that.aantal);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.artikel, this.aantal);
	}

	@Override
	public String toString() {
		return "<GebruiktEsselinkArtikel[" + String.valueOf(this.artikel) + ", "
				+ String.valueOf(this.aantal) + "]>";
	}
}
