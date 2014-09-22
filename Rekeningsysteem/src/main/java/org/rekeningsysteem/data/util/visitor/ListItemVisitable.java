package org.rekeningsysteem.data.util.visitor;

public interface ListItemVisitable {

	<T> T accept(ListItemVisitor<T> visitor);
}
