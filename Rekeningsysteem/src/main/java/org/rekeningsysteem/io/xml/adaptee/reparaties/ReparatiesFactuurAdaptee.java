package org.rekeningsysteem.io.xml.adaptee.reparaties;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.FactuurHeader;
import org.rekeningsysteem.io.xml.adapter.reparaties.ReparatiesItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.FactuurHeaderAdapter;

@XmlType(propOrder = { "factuurHeader", "valuta", "list", "btwPercentage" })
public class ReparatiesFactuurAdaptee {

	private FactuurHeader header;
	private String valuta;
	private ItemList<ReparatiesBon> list = new ItemList<>();
	private BtwPercentage btwPercentage;

	@XmlJavaTypeAdapter(FactuurHeaderAdapter.class)
	public FactuurHeader getFactuurHeader() {
		return this.header;
	}

	public void setFactuurHeader(FactuurHeader header) {
		this.header = header;
	}

	@XmlElement
	public String getValuta() {
		return this.valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
	}

	@XmlJavaTypeAdapter(ReparatiesItemListAdapter.class)
	public ItemList<ReparatiesBon> getList() {
		return this.list;
	}

	public void setList(ItemList<ReparatiesBon> list) {
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
