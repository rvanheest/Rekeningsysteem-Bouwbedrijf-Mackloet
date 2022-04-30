package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;

public class GebruiktEsselinkArtikel extends ParticulierArtikel {

	private final Geld prijs;
	private final BtwPercentage btwPercentage;
	private final EsselinkArtikel artikel;
	private final double aantal;

	public GebruiktEsselinkArtikel(EsselinkArtikel artikel, double aantal, BtwPercentage btwPercentage) {
		this(artikel.omschrijving(), artikel, aantal, btwPercentage);
	}

	public GebruiktEsselinkArtikel(String omschrijving, EsselinkArtikel artikel, double aantal, BtwPercentage btwPercentage) {
		super(omschrijving);
		this.prijs = artikel.verkoopPrijs().multiply(aantal).divide(artikel.prijsPer());
		this.btwPercentage = btwPercentage;
		this.aantal = aantal;
		this.artikel = artikel;
	}

	@Override
	public Geld materiaal() {
		return this.prijs;
	}

	@Override
	public BtwPercentage getMateriaalBtwPercentage() {
		return this.btwPercentage;
	}

	@Override
	public final Geld loon() {
		return new Geld(0);
	}

	@Override
	public final BtwPercentage getLoonBtwPercentage() {
		return new BtwPercentage(0, this.btwPercentage.verlegd());
	}

	@Override
	public final Geld getLoonBtw() {
		return new Geld(0);
	}

	public EsselinkArtikel getArtikel() {
		return this.artikel;
	}

	public double getAantal() {
		return this.aantal;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof GebruiktEsselinkArtikel that) {
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
		return Objects.hash(super.hashCode(), this.prijs, this.btwPercentage, this.aantal, this.artikel);
	}

	@Override
	public String toString() {
		return "<GebruiktEsselinkArtikel["
			+ String.valueOf(this.getOmschrijving()) + ", "
			+ String.valueOf(this.prijs) + ", "
			+ String.valueOf(this.btwPercentage) + ", "
			+ String.valueOf(this.aantal) + ", "
			+ String.valueOf(this.artikel) + "]>";
	}
}
