package org.rekeningsysteem.data.util;

import java.util.Objects;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitable;
import org.rekeningsysteem.logic.factuurnummer.FactuurnummerManager;

public abstract class AbstractRekening implements RekeningVisitable {

	private final FactuurHeader header;

	public AbstractRekening(FactuurHeader header) {
		this.header = header;
	}

	public FactuurHeader getFactuurHeader() {
		return this.header;
	}

	public void initFactuurnummer(FactuurnummerManager manager) {
		this.header.setFactuurnummer(this.header.getFactuurnummer()
				.orElseGet(() -> manager.getFactuurnummer()));
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractRekening) {
			AbstractRekening that = (AbstractRekening) other;
			return Objects.equals(this.header, that.header);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.header);
	}
}
