package org.rekeningsysteem.io.xml.adaptee.particulier2;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier2.ParticulierArtikel2;
import org.rekeningsysteem.io.xml.adapter.particulier2.ParticulierArtikel2Adapter;

public class ParticulierItem2ListAdaptee {

	private List<ParticulierArtikel2> list;

	@XmlElementRef
	@XmlJavaTypeAdapter(ParticulierArtikel2Adapter.class)
	public List<ParticulierArtikel2> getList() {
		return this.list;
	}

	public void setList(List<ParticulierArtikel2> list) {
		this.list = list;
	}
}
