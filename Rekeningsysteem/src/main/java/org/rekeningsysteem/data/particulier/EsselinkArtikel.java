package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.Geld;

public final class EsselinkArtikel {

	private final String artikelNummer;
	private final String omschrijving;
	private final int prijsPer;
	private final String eenheid;
	private final Geld verkoopPrijs;

	public EsselinkArtikel(String artikelNummer, String omschrijving, int prijsPer, String eenheid,
			Geld verkoopPrijs) {
		this.artikelNummer = artikelNummer;
		this.omschrijving = omschrijving;
		this.prijsPer = prijsPer;
		this.eenheid = eenheid;
		this.verkoopPrijs = verkoopPrijs;
	}

	public String getArtikelNummer() {
		return this.artikelNummer;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public int getPrijsPer() {
		return this.prijsPer;
	}

	public String getEenheid() {
		return this.eenheid;
	}

	public Geld getVerkoopPrijs() {
		return new Geld(this.verkoopPrijs);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof EsselinkArtikel) {
			EsselinkArtikel that = (EsselinkArtikel) other;
			return Objects.equals(this.artikelNummer, that.artikelNummer)
					&& Objects.equals(this.omschrijving, that.omschrijving)
					&& Objects.equals(this.prijsPer, that.prijsPer)
					&& Objects.equals(this.eenheid, that.eenheid)
					&& Objects.equals(this.verkoopPrijs, that.verkoopPrijs);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.artikelNummer, this.omschrijving, this.prijsPer, this.eenheid,
				this.verkoopPrijs);
	}

	@Override
	public String toString() {
		return "<EsselinkArtikel[" + String.valueOf(this.artikelNummer) + ", "
				+ String.valueOf(this.omschrijving) + ", "
				+ String.valueOf(this.prijsPer) + ", "
				+ String.valueOf(this.eenheid) + ", "
				+ String.valueOf(this.verkoopPrijs) + "]>";
	}
}
