package org.rekeningsysteem.data.util;

import java.util.Objects;

import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.data.util.visitor.RekeningVisitable;

public abstract class AbstractRekening implements RekeningVisitable {

	private final FactuurHeader header;

	public AbstractRekening(FactuurHeader header) {
		this.header = header;
	}

	public FactuurHeader getFactuurHeader() {
		return this.header;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof AbstractRekening that) {
			return Objects.equals(this.header, that.header);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.header);
	}
}
