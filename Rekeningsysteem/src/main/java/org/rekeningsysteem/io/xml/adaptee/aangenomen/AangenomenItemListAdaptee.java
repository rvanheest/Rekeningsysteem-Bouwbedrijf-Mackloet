package org.rekeningsysteem.io.xml.adaptee.aangenomen;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.io.xml.adapter.aangenomen.AangenomenListItemAdapter;

public class AangenomenItemListAdaptee {

	private List<AangenomenListItem> list;

	@XmlElement(name = "list-item")
	@XmlJavaTypeAdapter(AangenomenListItemAdapter.class)
	public List<AangenomenListItem> getList() {
		return this.list;
	}

	public void setList(List<AangenomenListItem> list) {
		this.list = list;
	}
}
