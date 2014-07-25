package org.rekeningsysteem.io.xml.adaptee.aangenomen;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.BtwPercentage;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.header.OmschrFactuurHeader;
import org.rekeningsysteem.io.xml.adapter.aangenomen.AangenomenItemListAdapter;
import org.rekeningsysteem.io.xml.adapter.util.BtwPercentageAdapter;
import org.rekeningsysteem.io.xml.adapter.util.header.OmschrFactuurHeaderAdapter;

@XmlType(propOrder = { "factuurHeader", "valuta", "list", "btwPercentage" })
public class AangenomenFactuurAdaptee {

	private OmschrFactuurHeader header;
	private String valuta;
	private ItemList<AangenomenListItem> list;
	private BtwPercentage btwPercentage;

	@XmlJavaTypeAdapter(OmschrFactuurHeaderAdapter.class)
	public OmschrFactuurHeader getFactuurHeader() {
		return this.header;
	}

	public void setFactuurHeader(OmschrFactuurHeader header) {
		this.header = header;
	}

	@XmlElement
	public String getValuta() {
		return this.valuta;
	}

	public void setValuta(String valuta) {
		this.valuta = valuta;
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
