package org.rekeningsysteem.io.xml.adaptee;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adapter.ListItemAdapter;

public class ItemListAdaptee {

	private List<ListItem> list;

	@XmlElementRef
	@XmlJavaTypeAdapter(ListItemAdapter.class)
	public List<ListItem> getList() {
		return this.list;
	}

	public void setList(List<ListItem> list) {
		this.list = list;
	}
}
