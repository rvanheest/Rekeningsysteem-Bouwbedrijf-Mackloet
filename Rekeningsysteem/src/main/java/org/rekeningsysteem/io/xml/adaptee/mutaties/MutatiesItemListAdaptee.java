package org.rekeningsysteem.io.xml.adaptee.mutaties;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.io.xml.adapter.mutaties.MutatiesBonAdapter;

public class MutatiesItemListAdaptee {

	private List<MutatiesBon> list;

	@XmlElement(name = "list-item")
	@XmlJavaTypeAdapter(MutatiesBonAdapter.class)
	public List<MutatiesBon> getList() {
		return this.list;
	}

	public void setList(List<MutatiesBon> list) {
		this.list = list;
	}
}
