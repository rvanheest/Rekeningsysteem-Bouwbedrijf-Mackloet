package org.rekeningsysteem.io.xml.adaptee.particulier;

import java.util.Currency;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.OmschrFactuurHeaderAdapter;
import org.rekeningsysteem.io.xml.adapter.util.loon.LoonListAdapter;

@XmlType(propOrder = { "factuurHeader", "currency", "itemList", "loonList", "btwPercentage" })
public class ParticulierFactuurAdaptee {

	private OmschrFactuurHeader factuurHeader;
	private Currency currency;
	private ItemList<ParticulierArtikel> itemList = new ItemList<>();
	private ItemList<AbstractLoon> loonList = new ItemList<>();
	private BtwPercentage btwPercentage;

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
	public ItemList<ParticulierArtikel> getItemList() {
		return this.itemList;
	}

	public void setItemList(ItemList<ParticulierArtikel> list) {
		this.itemList = list;
	}
	
	@XmlJavaTypeAdapter(LoonListAdapter.class)
	public ItemList<AbstractLoon> getLoonList() {
		return this.loonList;
	}

	public void setLoonList(ItemList<AbstractLoon> loonList) {
		this.loonList = loonList;
	}

	@XmlJavaTypeAdapter(BtwPercentageAdapter.class)
	public BtwPercentage getBtwPercentage() {
		return this.btwPercentage;
	}

	public void setBtwPercentage(BtwPercentage btwPercentage) {
		this.btwPercentage = btwPercentage;
	}
}
