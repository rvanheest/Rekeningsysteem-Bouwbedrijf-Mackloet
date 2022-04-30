package org.rekeningsysteem.data.particulier;

import java.util.Currency;

import org.rekeningsysteem.data.util.AbstractFactuur;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;

public class ParticulierFactuur extends AbstractFactuur<ParticulierArtikel> {

	private final String omschrijving;

	public ParticulierFactuur(FactuurHeader header, String omschrijving, Currency currency, ItemList<ParticulierArtikel> itemList) {
		super(header, currency, itemList);
		this.omschrijving = omschrijving;
	}

	public String getOmschrijving() {
		return this.omschrijving;
	}

	@Override
	public String toString() {
		return "<ParticulierFactuur["
			+ String.valueOf(this.getFactuurHeader()) + ", "
			+ String.valueOf(this.getCurrency()) + ", "
			+ String.valueOf(this.getItemList()) + "]>";
	}
}
