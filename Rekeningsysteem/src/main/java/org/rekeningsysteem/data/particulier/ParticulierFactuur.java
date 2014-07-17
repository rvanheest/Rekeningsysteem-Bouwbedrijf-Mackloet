package org.rekeningsysteem.data.particulier;

import java.util.List;
import java.util.Objects;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

public class ParticulierFactuur extends AbstractFactuur<ParticulierArtikel> {

	private final List<AbstractLoon> loonList;

	public ParticulierFactuur(OmschrFactuurHeader header, String valuta,
			List<ParticulierArtikel> itemList, BtwPercentage btwPercentage,
			List<AbstractLoon> loonList) {
		super(header, valuta, itemList, btwPercentage);
		this.loonList = loonList;
	}

	@Override
	public OmschrFactuurHeader getFactuurHeader() {
		return (OmschrFactuurHeader) super.getFactuurHeader();
	}

	public List<AbstractLoon> getLoonList() {
		return this.loonList;
	}

	@Override
	public void accept(RekeningVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof ParticulierFactuur) {
			ParticulierFactuur that = (ParticulierFactuur) other;
			return super.equals(that)
					&& Objects.equals(this.loonList, that.loonList);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.getLoonList());
	}

	@Override
	public String toString() {
		return "<ParticulierFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getValuta()) + ", "
				+ String.valueOf(this.getItemList()) + ", "
				+ String.valueOf(this.getBtwPercentage()) + ", "
				+ String.valueOf(this.loonList) + "]>";
	}
}
