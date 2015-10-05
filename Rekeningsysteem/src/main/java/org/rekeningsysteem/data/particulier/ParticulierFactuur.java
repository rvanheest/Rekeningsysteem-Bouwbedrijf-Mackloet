package org.rekeningsysteem.data.particulier;

import java.util.Currency;
import java.util.Objects;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.Totalen;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;

@Deprecated
public class ParticulierFactuur extends AbstractFactuur<ParticulierArtikel> {

	private final ItemList<AbstractLoon> loonList;

	public ParticulierFactuur(OmschrFactuurHeader header, Currency currency,
			ItemList<ParticulierArtikel> itemList, ItemList<AbstractLoon> loonList) {
		super(header, currency, itemList);
		this.loonList = loonList;
	}

	@Override
	public OmschrFactuurHeader getFactuurHeader() {
		return (OmschrFactuurHeader) super.getFactuurHeader();
	}

	public ItemList<AbstractLoon> getLoonList() {
		return this.loonList;
	}

	@Override
	public Totalen getTotalen() {
		return super.getTotalen().plus(this.loonList.getTotalen());
	}

	@Override
	public void accept(RekeningVisitor visitor) throws Exception {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object other) {
		if (super.equals(other) && other instanceof ParticulierFactuur) {
			ParticulierFactuur that = (ParticulierFactuur) other;
			return Objects.equals(this.loonList, that.loonList);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.loonList);
	}

	@Override
	public String toString() {
		return "<ParticulierFactuur[" + String.valueOf(this.getFactuurHeader()) + ", "
				+ String.valueOf(this.getCurrency()) + ", "
				+ String.valueOf(this.getItemList()) + ", "
				+ String.valueOf(this.loonList) + "]>";
	}
}
