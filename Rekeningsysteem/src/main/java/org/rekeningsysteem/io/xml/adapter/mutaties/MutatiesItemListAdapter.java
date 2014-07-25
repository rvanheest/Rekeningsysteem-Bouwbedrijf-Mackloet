package org.rekeningsysteem.io.xml.adapter.mutaties;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.mutaties.MutatiesBon;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.mutaties.MutatiesItemListAdaptee;

public class MutatiesItemListAdapter extends
		XmlAdapter<MutatiesItemListAdaptee, ItemList<MutatiesBon>> {

	@Override
	public ItemList<MutatiesBon> unmarshal(MutatiesItemListAdaptee v) {
		return new ItemList<>(v.getList());
	}

	@Override
	public MutatiesItemListAdaptee marshal(ItemList<MutatiesBon> v) {
		MutatiesItemListAdaptee adaptee = new MutatiesItemListAdaptee();
		adaptee.setList(new ArrayList<>(v));
		return adaptee;
	}
}
