package org.rekeningsysteem.io.xml.adaptee.reparaties;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.io.xml.adapter.reparaties.ReparatiesBonAdapter;

public class ReparatiesItemListAdaptee {

	private List<ReparatiesBon> list;

	@XmlElement(name = "list-item")
	@XmlJavaTypeAdapter(ReparatiesBonAdapter.class)
	public List<ReparatiesBon> getList() {
		return this.list;
	}

	public void setList(List<ReparatiesBon> list) {
		this.list = list;
	}
}
