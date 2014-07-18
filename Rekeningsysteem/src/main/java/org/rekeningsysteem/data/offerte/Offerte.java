package org.rekeningsysteem.data.offerte;

import java.util.Objects;

import org.rekeningsysteem.data.util.AbstractRekening;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class Offerte extends AbstractRekening {

	private String tekst;
	private boolean ondertekenen;

	public Offerte(FactuurHeader header, String tekst, boolean ondertekenen) {
		super(header);
		this.setTekst(tekst);
		this.setOndertekenen(ondertekenen);
	}

	public String getTekst() {
		return this.tekst;
	}

	public final void setTekst(String tekst) {
		this.tekst = tekst;
	}

	public boolean isOndertekenen() {
		return this.ondertekenen;
	}

	public final void setOndertekenen(boolean ondertekenen) {
		this.ondertekenen = ondertekenen;
	}

	@Override
	public void accept(RekeningVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Offerte) {
			Offerte that = (Offerte) other;
			return super.equals(that)
					&& Objects.equals(this.tekst, that.tekst)
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