package org.rekeningsysteem.data.util;

import org.rekeningsysteem.data.util.visitor.ListItemVisitable;

public abstract class ListItem implements ListItemVisitable {

	public abstract Geld getLoon();

	public abstract Geld getMateriaal();

	public Geld getTotaal() {
		return this.getLoon().add(this.getMateriaal());
	}
}
