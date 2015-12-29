package org.rekeningsysteem.io.xml.adaptee.particulier;

import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.io.xml.adapter.particulier.ParticulierArtikelAdapter;

public class ParticulierItemListAdaptee {

	private List<ParticulierArtikel> list;

	@XmlElementRef
	@XmlJavaTypeAdapter(ParticulierArtikelAdapter.class)
	public List<ParticulierArtikel> getList() {
		return this.list;
	}

	public void setList(List<ParticulierArtikel> list) {
		this.list = list;
	}
}
