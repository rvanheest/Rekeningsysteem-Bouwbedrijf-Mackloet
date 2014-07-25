package org.rekeningsysteem.io.xml.adapter.aangenomen;

import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.aangenomen.AangenomenListItem;
import org.rekeningsysteem.data.util.ItemList;
import org.rekeningsysteem.io.xml.adaptee.aangenomen.AangenomenItemListAdaptee;

public class AangenomenItemListAdapter extends
		XmlAdapter<AangenomenItemListAdaptee, ItemList<AangenomenListItem>> {

	@Override
	public ItemList<AangenomenListItem> unmarshal(AangenomenItemListAdaptee v) {
		return new ItemList<>(v.getList());
	}

	@Override
	public AangenomenItemListAdaptee marshal(ItemList<AangenomenListItem> v) {
		AangenomenItemListAdaptee adaptee = new AangenomenItemListAdaptee();
		adaptee.setList(new ArrayList<>(v));
		return adaptee;
	}
}
