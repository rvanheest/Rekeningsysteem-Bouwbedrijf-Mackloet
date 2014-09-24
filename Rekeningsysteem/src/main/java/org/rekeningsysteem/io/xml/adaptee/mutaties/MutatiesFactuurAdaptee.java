package org.rekeningsysteem.io.xml.adaptee.mutaties;

import java.util.Currency;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adapter.mutaties.MutatiesItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.FactuurHeaderAdapter;

@XmlType(propOrder = { "factuurHeader", "currency", "list", "btwPercentage" })
public class MutatiesFactuurAdaptee {

	private FactuurHeader header;
	private Currency currency;
	private ItemList<MutatiesBon> list = new ItemList<>();
	private BtwPercentage btwPercentage;

	@XmlJavaTypeAdapter(FactuurHeaderAdapter.class)
	public FactuurHeader getFactuurHeader() {
		return this.header;
	}

	public void setFactuurHeader(FactuurHeader header) {
		this.header = header;
	}

	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlJavaTypeAdapter(MutatiesItemListAdapter.class)
	public ItemList<MutatiesBon> getList() {
		return this.list;
	}

	public void setList(ItemList<MutatiesBon> list) {
		this.list = list;
	}

	@XmlJavaTypeAdapter(BtwPercentageAdapter.class)
	public BtwPercentage getBtwPercentage() {
		return this.btwPercentage;
	}

	public void setBtwPercentage(BtwPercentage btwPercentage) {
		this.btwPercentage = btwPercentage;
	}
}
