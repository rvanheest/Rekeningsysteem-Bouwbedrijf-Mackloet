package org.rekeningsysteem.io.xml.adaptee.particulier2;

import java.util.Currency;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.particulier2.ParticulierItem2ListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.OmschrFactuurHeaderAdapter;

@XmlType(propOrder = { "factuurHeader", "currency", "list" })
public class ParticulierFactuur2Adaptee {

	private OmschrFactuurHeader factuurHeader;
	private Currency currency;
	private ItemList<ParticulierArtikel2> list = new ItemList<>();

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

	@XmlJavaTypeAdapter(ParticulierItem2ListAdapter.class)
	public ItemList<ParticulierArtikel2> getList() {
		return this.list;
	}

	public void setList(ItemList<ParticulierArtikel2> list) {
		this.list = list;
	}
}
