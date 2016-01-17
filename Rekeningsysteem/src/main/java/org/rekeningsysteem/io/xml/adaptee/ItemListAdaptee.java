package org.rekeningsysteem.io.xml.adaptee;

import java.util.List;
import java.util.function.Function;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.io.xml.adapter.ListItemAdapter;

public class ItemListAdaptee {

	private List<ListItem> list;

	private ItemListAdaptee() {}

	@XmlElementRef
	@XmlJavaTypeAdapter(ListItemAdapter.class)
	public List<ListItem> getList() {
		return this.list;
	}

	public ItemListAdaptee setList(List<ListItem> list) {
		this.list = list;
		return this;
	}

	public static ItemListAdaptee build(Function<ItemListAdaptee, ItemListAdaptee> builder) {
		return builder.apply(new ItemListAdaptee());
	}
}
