package org.rekeningsysteem.io.xml.adaptee.particulier;

import java.util.Currency;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.OmschrFactuurHeaderAdapter;

@XmlType(propOrder = { "factuurHeader", "currency", "list" })
public class ParticulierFactuurAdaptee {

	private OmschrFactuurHeader factuurHeader;
	private Currency currency;
	private ItemList<ParticulierArtikel> list = new ItemList<>();

	@XmlJavaTypeAdapter(OmschrFactuurHeaderAdapter.class)
	public OmschrFactuurHeader getFactuurHeader() {
		return this.factuurHeader;
	}

	public void setFactuurHeader(OmschrFactuurHeader factuurHeader) {
		this.factuurHeader = factuurHeader;
	}

	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlJavaTypeAdapter(ParticulierItemListAdapter.class)
	public ItemList<ParticulierArtikel> getList() {
		return this.list;
	}

	public void setList(ItemList<ParticulierArtikel> list) {
		this.list = list;
	}
}