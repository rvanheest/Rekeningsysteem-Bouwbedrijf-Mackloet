package org.rekeningsysteem.io.xml.adaptee.mutaties;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.io.xml.adapter.ListItemAdapter;

public class MutatiesItemListAdaptee {

	private List<MutatiesBon> list;

	@XmlElementRef
	@XmlJavaTypeAdapter(ListItemAdapter.class)
	public List<MutatiesBon> getList() {
		return this.list;
	}

	public void setList(List<MutatiesBon> list) {
		this.list = list;
	}
}
