package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public final class GebruiktEsselinkArtikel extends ParticulierArtikel {

	private final EsselinkArtikel artikel;
	private final double aantal;
	private final Geld materiaal;
	private final double btwPercentage;

	public GebruiktEsselinkArtikel(EsselinkArtikel artikel, double aantal, double btwPercentage) {
		this.artikel = artikel;
		this.aantal = aantal;
		this.materiaal = this.artikel.getVerkoopPrijs().multiply(this.aantal)
				.divide(this.artikel.getPrijsPer());
		this.btwPercentage = btwPercentage;
	}

	public EsselinkArtikel getArtikel() {
		return this.artikel;
	}

	public double getAantal() {
		return this.aantal;
	}

	@Override
	public Geld getMateriaal() {
		return new Geld(this.materiaal);
	}

	@Override
	public double getMateriaalBtwPercentage() {
		return this.btwPercentage;
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
					&& Objects.equals(this.aantal, that.aantal)
					&& Objects.equals(this.btwPercentage, that.btwPercentage);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.artikel, this.aantal, this.btwPercentage);
	}

	@Override
	public String toString() {
		return "<GebruiktEsselinkArtikel[" + String.valueOf(this.artikel) + ", "
				+ String.valueOf(this.aantal) + ", "
				+ String.valueOf(this.btwPercentage) + "]>";
	}
}
