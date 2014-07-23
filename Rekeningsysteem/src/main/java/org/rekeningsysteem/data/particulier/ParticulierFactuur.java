package org.rekeningsysteem.data.particulier;

import java.util.Objects;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.Geld;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.data.util.visitor.RekeningVisitor;
import org.rekeningsysteem.logic.bedragmanager.Totalen;

public class ParticulierFactuur extends AbstractFactuur<ParticulierArtikel> {

	private final ItemList<AbstractLoon> loonList;

	public ParticulierFactuur(OmschrFactuurHeader header, String valuta,
			ItemList<ParticulierArtikel> itemList, ItemList<AbstractLoon> loonList,
			BtwPercentage btwPercentage) {
		super(header, valuta, itemList, btwPercentage);
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
		Totalen sub = this.loonList.getTotalen();
		Geld loonBtw = sub.getLoon()
				.multiply(this.getBtwPercentage().getLoonPercentage())
				.divide(100);
		Geld materiaalBtw = sub.getMateriaal()
				.multiply(this.getBtwPercentage().getMateriaalPercentage())
				.divide(100);
		Totalen loon = sub.withLoonBtw(loonBtw)
				.withMateriaalBtw(materiaalBtw);
		return super.getTotalen().plus(loon);
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
				+ String.valueOf(this.getValuta()) + ", "
				+ String.valueOf(this.getItemList()) + ", "
				+ String.valueOf(this.loonList) + ", "
				+ String.valueOf(this.getBtwPercentage()) + "]>";
	}
}
