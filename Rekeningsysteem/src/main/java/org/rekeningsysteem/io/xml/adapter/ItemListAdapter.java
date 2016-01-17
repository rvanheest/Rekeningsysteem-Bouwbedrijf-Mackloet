package org.rekeningsysteem.io.xml.adapter;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adaptee.ItemListAdaptee;

public class ItemListAdapter extends XmlAdapter<ItemListAdaptee, ItemList<ListItem>> {

	@Override
	public ItemList<ListItem> unmarshal(ItemListAdaptee v) {
		return new ItemList<>(v.getList());
	}

	@Override
	public ItemListAdaptee marshal(ItemList<ListItem> v) {
		return ItemListAdaptee.build(adaptee -> adaptee
				.setList(new ArrayList<>(v))); // TODO this can be setList(v) as well, right?
	}
}
