package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

public class GebruiktEsselinkArtikel extends ParticulierArtikel {

	private final Geld prijs;
	private final double btwPercentage;
	private final EsselinkArtikel artikel;
	private final double aantal;

	public GebruiktEsselinkArtikel(EsselinkArtikel artikel, double aantal, double btwPercentage) {
		this(artikel.getOmschrijving(), artikel, aantal, btwPercentage);
	}

	public GebruiktEsselinkArtikel(String omschrijving, EsselinkArtikel artikel,
			double aantal, double btwPercentage) {
		super(omschrijving);
		this.prijs = artikel.getVerkoopPrijs().multiply(aantal).divide(artikel.getPrijsPer());
		this.btwPercentage = btwPercentage;
		this.aantal = aantal;
		this.artikel = artikel;
	}

	@Override
	public Geld getMateriaal() {
		return this.prijs;
	}

	@Override
	public double getMateriaalBtwPercentage() {
		return this.btwPercentage;
	}

	@Override
	public final Geld getLoon() {
		return new Geld(0);
	}

	@Override
	public final double getLoonBtwPercentage() {
		return 0;
	}

	public EsselinkArtikel getArtikel() {
		return this.artikel;
	}

	public double getAantal() {
    	return this.aantal;
    }

	@Override
	public <T> T accept(ListItemVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof GebruiktEsselinkArtikel) {
			GebruiktEsselinkArtikel that = (GebruiktEsselinkArtikel) other;
			return super.equals(that)
					&& Objects.equals(this.prijs, that.prijs)
					&& Objects.equals(this.btwPercentage, that.btwPercentage)
					&& Objects.equals(this.aantal, that.aantal)
					&& Objects.equals(this.artikel, that.artikel);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.prijs, this.btwPercentage,
				this.aantal, this.artikel);
	}

	@Override
	public String toString() {
		return "<GebruiktEsselinkArtikel[" + String.valueOf(this.getOmschrijving()) + ", "
				+ String.valueOf(this.prijs) + ", "
				+ String.valueOf(this.btwPercentage) + ", "
				+ String.valueOf(this.aantal) + ", "
				+ String.valueOf(this.artikel) + "]>";
	}
}
