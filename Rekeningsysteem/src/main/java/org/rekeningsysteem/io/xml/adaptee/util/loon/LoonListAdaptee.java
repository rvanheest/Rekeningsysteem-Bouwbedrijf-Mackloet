package org.rekeningsysteem.io.xml.adaptee.util.loon;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.io.xml.adapter.util.loon.LoonAdapter;

@Deprecated
public class LoonListAdaptee {

	private List<AbstractLoon> list;

	@XmlElementRef
	@XmlJavaTypeAdapter(LoonAdapter.class)
	public List<AbstractLoon> getList() {
		return this.list;
	}

	public void setList(List<AbstractLoon> list) {
		this.list = list;
	}
}
