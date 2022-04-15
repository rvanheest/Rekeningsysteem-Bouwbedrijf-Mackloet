package org.rekeningsysteem.data.util;

import org.rekeningsysteem.data.util.visitor.ListItemVisitable;

public interface ListItem extends ListItemVisitable {

	Geld loon();

	Geld materiaal();
}
