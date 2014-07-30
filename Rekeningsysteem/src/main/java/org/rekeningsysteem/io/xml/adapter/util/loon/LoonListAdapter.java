package org.rekeningsysteem.io.xml.adapter.util.loon;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.loon.AbstractLoon;
import org.rekeningsysteem.io.xml.adaptee.util.loon.LoonListAdaptee;

public class LoonListAdapter extends XmlAdapter<LoonListAdaptee, ItemList<AbstractLoon>> {

	@Override
	public ItemList<AbstractLoon> unmarshal(LoonListAdaptee v) {
		return new ItemList<>(v.getList());
	}

	@Override
	public LoonListAdaptee marshal(ItemList<AbstractLoon> v) {
		LoonListAdaptee adaptee = new LoonListAdaptee();
		adaptee.setList(new ArrayList<>(v));
		return adaptee;
	}
}
