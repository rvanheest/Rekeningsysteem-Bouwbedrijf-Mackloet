package org.rekeningsysteem.io.xml.adapter.particulier;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier.ParticulierArtikel2;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierItem2ListAdaptee;

public class ParticulierItem2ListAdapter extends
		XmlAdapter<ParticulierItem2ListAdaptee, ItemList<ParticulierArtikel2>> {

	@Override
	public ItemList<ParticulierArtikel2> unmarshal(ParticulierItem2ListAdaptee v) {
		return new ItemList<>(v.getList());
	}

	@Override
	public ParticulierItem2ListAdaptee marshal(ItemList<ParticulierArtikel2> v) {
		ParticulierItem2ListAdaptee adaptee = new ParticulierItem2ListAdaptee();
		adaptee.setList(new ArrayList<>(v));
		return adaptee;
	}

}
