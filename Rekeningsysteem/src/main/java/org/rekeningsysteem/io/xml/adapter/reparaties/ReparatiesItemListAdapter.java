package org.rekeningsysteem.io.xml.adapter.reparaties;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.reparaties.ReparatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.reparaties.ReparatiesItemListAdaptee;

public class ReparatiesItemListAdapter extends
		XmlAdapter<ReparatiesItemListAdaptee, ItemList<ReparatiesBon>> {

	@Override
	public ItemList<ReparatiesBon> unmarshal(ReparatiesItemListAdaptee v) {
		return new ItemList<>(v.getList());
	}

	@Override
	public ReparatiesItemListAdaptee marshal(ItemList<ReparatiesBon> v) {
		ReparatiesItemListAdaptee adaptee = new ReparatiesItemListAdaptee();
		adaptee.setList(new ArrayList<>(v));
		return adaptee;
	}
}
