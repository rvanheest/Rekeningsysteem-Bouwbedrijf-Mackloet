package org.rekeningsysteem.io.xml.adaptee.aangenomen;

import java.util.Currency;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.aangenomen.AangenomenItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;
import org.rekeningsysteem.io.xml.adapter.util.CurrencyAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.OmschrFactuurHeaderAdapter;

@XmlType(propOrder = { "factuurHeader", "currency", "list", "btwPercentage" })
public class AangenomenFactuurAdaptee {

	private OmschrFactuurHeader header;
	private Currency currency;
	private ItemList<AangenomenListItem> list = new ItemList<>();
	private BtwPercentage btwPercentage;

	@XmlJavaTypeAdapter(OmschrFactuurHeaderAdapter.class)
	public OmschrFactuurHeader getFactuurHeader() {
		return this.header;
	}

	public void setFactuurHeader(OmschrFactuurHeader header) {
		this.header = header;
	}

	@XmlJavaTypeAdapter(CurrencyAdapter.class)
	public Currency getCurrency() {
		return this.currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@XmlJavaTypeAdapter(AangenomenItemListAdapter.class)
	public ItemList<AangenomenListItem> getList() {
		return this.list;
	}

	public void setList(ItemList<AangenomenListItem> list) {
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
