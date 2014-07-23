package org.rekeningsysteem.data.util.header;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class FactuurHeader {

	private Debiteur debiteur;
	private LocalDate datum;
	private Optional<String> factuurnummer;

	public FactuurHeader(Debiteur debiteur, LocalDate datum, String factuurnummer) {
		this(debiteur, datum, Optional.of(factuurnummer));
	}

	public FactuurHeader(Debiteur debiteur, LocalDate datum) {
		this(debiteur, datum, Optional.empty());
	}
	
	private FactuurHeader(Debiteur debiteur, LocalDate datum, Optional<String> factuurnummer) {
		this.debiteur = debiteur;
		this.datum = datum;
		this.factuurnummer = factuurnummer;
	}

	public Debiteur getDebiteur() {
		return this.debiteur;
	}

	public void setDebiteur(Debiteur debiteur) {
		this.debiteur = debiteur;
	}

	public LocalDate getDatum() {
		return this.datum;
	}

	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}

	public Optional<String> getFactuurnummer() {
		return this.factuurnummer;
	}

	public void setFactuurnummer(String factuurnummer) {
		this.factuurnummer = Optional.ofNullable(factuurnummer);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof FactuurHeader) {
			FactuurHeader that = (FactuurHeader) other;
			return Objects.equals(this.debiteur, that.debiteur)
					&& Objects.equals(this.datum, that.datum)
					&& Objects.equals(this.factuurnummer, that.factuurnummer);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.debiteur, this.datum, this.factuurnummer);
	}

	@Override
	public String toString() {
		return "<FactuurHeader[" + String.valueOf(this.debiteur) + ", "
				+ String.valueOf(this.datum) + ", "
				+ String.valueOf(this.factuurnummer) + "]>";
	}
}
