package org.rekeningsysteem.data.particulier2;

import java.util.Objects;

import org.rekeningsysteem.data.particulier.EsselinkArtikel;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;

// TODO GebruiktEsselinkArtikel
public class EsselinkParticulierArtikel implements ParticulierArtikel2 {

	private final String omschrijving;
	private final Geld prijs;
	private final double btwPercentage;
	private final EsselinkArtikel artikel;
	private final double aantal;

	public EsselinkParticulierArtikel(EsselinkArtikel artikel, double aantal, double btwPercentage) {
		this(artikel.getOmschrijving(), artikel, aantal, btwPercentage);
	}

	public EsselinkParticulierArtikel(String omschrijving, EsselinkArtikel artikel,
			double aantal, double btwPercentage) {
		this.omschrijving = omschrijving;
		this.prijs = artikel.getVerkoopPrijs().multiply(aantal).divide(artikel.getPrijsPer());
		this.btwPercentage = btwPercentage;
		this.aantal = aantal;
		this.artikel = artikel;
	}
	
	public String getOmschrijving() {
		return this.omschrijving;
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
		if (other instanceof EsselinkParticulierArtikel) {
			EsselinkParticulierArtikel that = (EsselinkParticulierArtikel) other;
			return Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.prijs, that.prijs)
					&& Objects.equals(this.btwPercentage, that.btwPercentage)
					&& Objects.equals(this.aantal, that.aantal)
					&& Objects.equals(this.artikel, that.artikel);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.omschrijving, this.prijs, this.btwPercentage,
				this.aantal, this.artikel);
	}

	@Override
	public String toString() {
		return "<EsselinkParticulierArtikel[" + String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.prijs) + ", "
				+ String.valueOf(this.btwPercentage) + ", "
				+ String.valueOf(this.aantal) + ", "
				+ String.valueOf(this.artikel) + "]>";
	}
}
