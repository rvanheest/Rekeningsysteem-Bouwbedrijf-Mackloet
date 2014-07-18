package org.rekeningsysteem.data.util.header;

import java.time.LocalDate;
import java.util.Objects;

public class OmschrFactuurHeader extends FactuurHeader {

	private String omschrijving;

	public OmschrFactuurHeader(Debiteur debiteur, LocalDate datum, String factuurnummer,
			String omschrijving) {
		super(debiteur, datum, factuurnummer);
		this.omschrijving = omschrijving;
	}

	public OmschrFactuurHeader(Debiteur debiteur, LocalDate datum, String omschrijving) {
		super(debiteur, datum);
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	public void setOmschrijving(String omschrijving) {
		this.omschrijving = omschrijving;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof OmschrFactuurHeader) {
			OmschrFactuurHeader that = (OmschrFactuurHeader) other;
			return super.equals(that)
					&& Objects.equals(this.omschrijving, that.omschrijving);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.omschrijving);
	}

	@Override
	public String toString() {
		return "<FactuurHeader[" + String.valueOf(this.getDebiteur()) + ", "
				+ String.valueOf(this.getDatum()) + ", "
				+ String.valueOf(this.getFactuurnummer()) + ", "
				+ String.valueOf(this.omschrijving) + "]>";
	}
}
