package org.rekeningsysteem.io.xml.adapter.particulier;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.particulier.ParticulierArtikel;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.particulier.ParticulierItemListAdaptee;

@Deprecated
public class ParticulierItemListAdapter extends
		XmlAdapter<ParticulierItemListAdaptee, ItemList<ParticulierArtikel>> {

	@Override
	public ItemList<ParticulierArtikel> unmarshal(ParticulierItemListAdaptee v) {
		return new ItemList<>(v.getList());
	}

	@Override
	public ParticulierItemListAdaptee marshal(ItemList<ParticulierArtikel> v) {
		ParticulierItemListAdaptee adaptee = new ParticulierItemListAdaptee();
		adaptee.setList(new ArrayList<>(v));
		return adaptee;
	}
}
