package org.rekeningsysteem.io.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.rekeningsysteem.data.util.ListItem;
import org.rekeningsysteem.data.util.visitor.ListItemVisitor;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitable;
import org.rekeningsysteem.io.xml.adaptee.ListItemAdapteeVisitor;

public class ListItemAdapter extends XmlAdapter<ListItemAdapteeVisitable, ListItem> {

	private final ListItemAdapteeVisitor<ListItem> unmarshalVisitor = new UnmarshalListItemVisitor();
	private final ListItemVisitor<ListItemAdapteeVisitable> marshalVisitor = new MarshalListItemVisitor();

	@Override
	public ListItem unmarshal(ListItemAdapteeVisitable adaptee) {
		return adaptee.accept(this.unmarshalVisitor);
	}

	@Override
	public ListItemAdapteeVisitable marshal(ListItem listItem) {
		return listItem.accept(this.marshalVisitor);
	}
}
