package org.rekeningsysteem.data.offerte;

import java.util.Objects;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class Offerte extends AbstractRekening {

	private final String tekst;
	private final boolean ondertekenen;

	public Offerte(FactuurHeader header, String tekst, boolean ondertekenen) {
		super(header);
		this.tekst = tekst;
		this.ondertekenen = ondertekenen;
	}

	public String getTekst() {
		return this.tekst;
	}

	public boolean isOndertekenen() {
		return this.ondertekenen;
	}

	@Override
	public <T> T accept(RekeningVisitor<T> visitor) throws Exception {
		return visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof Offerte) {
			Offerte that = (Offerte) other;
			return Objects.equals(this.tekst, that.tekst)
					&& Objects.equals(this.ondertekenen, that.ondertekenen);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.tekst, this.ondertekenen);
	}

	@Override
	public String toString() {
		return "<Offerte[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.tekst) + ", "
				+ String.valueOf(this.ondertekenen) + "]>";
	}
}
